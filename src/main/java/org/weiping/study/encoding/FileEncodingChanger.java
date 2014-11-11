package org.weiping.study.encoding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

public class FileEncodingChanger {
	private static String sourcePath;
	private static String backupPath;
	private static String sourceEncoding = "gbk";
	private static String targetEncoding = "utf-8";
	
	public static void main(String[] args) {
		// 需要转换的文件目录
		sourcePath = args[0];
		// 转换到指定的文件目录
		backupPath = args[1];
		if (args.length >= 3) {
			targetEncoding = args[2];
		}

		String[] array = { sourcePath, backupPath };
		info("start transform [from path]={0} [to path]={1}", array);

		// 递归取到所有的文件进行转换
		transform(sourcePath, backupPath);
	}

	/**
	 * 把一个目录中的文件转换到另一个目录中
	 * 
	 * @param sourceFile
	 *            -- 来源文件目录
	 * @param backupFile
	 *            -- 备份文件目录
	 * @return
	 */
	public static boolean transform(String sourceFile, String backupFile) {
		File ftmp = new File(sourceFile);
		if (!ftmp.exists()) {
			info("转换文件路径错误！", null);
			return false;
		}
		String[] array = { sourceFile, backupFile };
		info("sourceFile is [{0}], backupFile is [{1}]", array);

		// 如果是文件，则转换，结束
		if (ftmp.isFile()) {
			byte[] value = fileBackupAndToBytes(sourceFile, backupFile);
			String content = convEncoding(value);
			return saveFile(sourceFile, content);
		} else {
			// 查找目录下面的所有文件与文件夹
			if (ftmp.getName().equalsIgnoreCase(".svn")
					|| ftmp.getName().equalsIgnoreCase("plugins")
					|| ftmp.getName().equalsIgnoreCase("jquery-ui")
					|| ftmp.getName().equalsIgnoreCase("plugins")) {
				info("忽略svn文件夹 [{0}]", new Object[] {ftmp.getAbsolutePath()});
				return false;
			}
			if (ftmp.getName().equalsIgnoreCase(".svn")) {
				info("忽略svn文件夹", null);
				return false;
			}
			File[] childFiles = ftmp.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					System.out.println(name);
					boolean isAccept = name.endsWith(".java")
							|| name.endsWith(".js")
							|| name.endsWith(".css")
							|| name.endsWith(".html")
							|| name.endsWith(".jsp")
							|| name.endsWith(".properties")
							|| name.endsWith(".xml");
					if (!isAccept) {
						isAccept = new File(dir.getAbsolutePath() + "/" + name).isDirectory();
					}
					return isAccept;
				}
			});
			File backupDir = new File(backupFile);
			if (!backupDir.exists()) {
				backupDir.mkdirs();
			}
			for (int i = 0, n = childFiles.length; i < n; i++) {
				File child = childFiles[i];
				String childSource = sourceFile + "/" + child.getName();
				String childBackup = backupFile + "/" + child.getName();

				transform(childSource, childBackup);
			}
		}

		return true;
	}

	/**
	 * 把文件内容保存到指定的文件中，如果指定的文件已存在，则先删除这个文件， 如果没有则创建一个新文件，文件内容采用UTF-8编码方式保存。
	 * 如果指定的文件路径不存在，则先创建文件路径，文件路径从根目录开始创建。
	 * 
	 * @param filePath
	 *            -- 文件路径
	 * @param content
	 *            -- 文件内容
	 * @return
	 */
	public static boolean saveFile(String filePath, String content) {
		if (filePath == null || filePath.length() == 0)
			return false;
		if (content == null)
			return false;

		// 路径中的\转换为/
		filePath = filePath.replace('\\', '/');
		// 处理文件路径
		createPath(filePath.substring(0, filePath.lastIndexOf('/')));

		File file = null;
		FileOutputStream out = null;
		try {
			// 创建或修改文件
			file = new File(filePath);

			if (file.exists()) {
				file.delete();
			} else {
				file.createNewFile();
			}

			out = new FileOutputStream(file);
			// 添加三个字节标识为UTF-8格式，也是BOM码
			// out.write(new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF});
			out.write(content.getBytes(targetEncoding));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 备份文件,并把文件内容转换为字节数组输出。
	 * 
	 * @param sourceFile
	 *            -- 源文件名
	 * @param backupFile
	 *            -- 备份文件名
	 * @return
	 */
	public static byte[] fileBackupAndToBytes(String sourceFile, String backupFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ByteArrayOutputStream bos = null;
		try {
			// 创建文件读入流
			fis = new FileInputStream(new File(sourceFile));
			sourceEncoding = JudgeFileCode.detecte(sourceFile).name();
			// 创建目标输出流
			bos = new ByteArrayOutputStream();
			File backup = new File(backupFile);
			if (!backup.exists()) {
				backup.createNewFile();
			}
			fos = new FileOutputStream(backup);

			// 取流中的数据
			int len = 0;
			byte[] buf = new byte[256];
			while ((len = fis.read(buf, 0, 256)) > -1) {
				bos.write(buf, 0, len);
				fos.write(buf, 0, len);
			}

			// 目标流转为字节数组返回到前台
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
				if (bos != null) {
					bos.close();
					bos = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 检查指定的文件路径，如果文件路径不存在，则创建新的路径， 文件路径从根目录开始创建。
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean createPath(String filePath) {
		if (filePath == null || filePath.length() == 0)
			return false;

		// 路径中的\转换为/
		filePath = filePath.replace('\\', '/');
		// 处理文件路径
		String[] paths = filePath.split("/");

		// 处理文件名中没有的路径
		// / StringBuilder sbpath = new StringBuilder();
		StringBuffer sbpath = new StringBuffer();
		for (int i = 0, n = paths.length; i < n; i++) {
			sbpath.append(paths[i]);
			// 检查文件路径如果没有则创建
			File ftmp = new File(sbpath.toString());
			if (!ftmp.exists()) {
				ftmp.mkdir();
			}

			sbpath.append("/");
		}

		return true;
	}

	/**
	 * 取路径中的文件名
	 * 
	 * @param path
	 *            -- 文件路径，含文件名
	 * @return
	 */
	public static String getFileName(String path) {
		if (path == null || path.length() == 0)
			return "";

		path = path.replaceAll("\\\\", "/");
		int last = path.lastIndexOf("/");

		if (last >= 0) {
			return path.substring(last + 1);
		} else {
			return path;
		}
	}

	/**
	 * 字符串的编码格式转换
	 * 
	 * @param value
	 *            -- 要转换的字符串
	 * @param oldCharset
	 *            -- 原编码格式
	 * @param newCharset
	 *            -- 新编码格式
	 * @return
	 */
	public static String convEncoding(byte[] value) {
		OutputStreamWriter outWriter = null;
		ByteArrayInputStream byteIns = null;
		ByteArrayOutputStream byteOuts = new ByteArrayOutputStream();
		InputStreamReader inReader = null;

		char cbuf[] = new char[1024];
		int retVal = 0;
		try {
			byteIns = new ByteArrayInputStream(value);
			inReader = new InputStreamReader(byteIns, sourceEncoding);
			outWriter = new OutputStreamWriter(byteOuts, targetEncoding);
			while ((retVal = inReader.read(cbuf)) != -1) {
				outWriter.write(cbuf, 0, retVal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inReader != null)
					inReader.close();
				if (outWriter != null)
					outWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String temp = null;
		try {
			temp = new String(byteOuts.toByteArray(), targetEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// System.out.println("temp" + temp);
		return temp;
	}

	/**
	 * 显示提示信息
	 * 
	 * @param message
	 *            -- 信息内容
	 * @param params
	 *            -- 参数
	 */
	private static void info(String message, Object[] params) {
		message = MessageFormat.format(message, params);

		System.out.println(message);
	}
}
