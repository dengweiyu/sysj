package com.li.videoapplication.data.local;

import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.utils.StringUtil;

import java.util.List;

/**
 * 实体：图文文件夹
 */
public class ImageDirectoryEntity extends BaseEntity {

    /**
     * 当前文件夹路径
     */
	private String path;

    /**
     * 当前文件夹名称
     */
    private String name;

    /**
     * 当前文件夹第一张图片文件名
     */
	private String firstFileName;

    /**
     * 当前文件夹中图片文件名
     */
    private List<String> fileNames;

    /**
     * 当前文件夹中图片的数量
     */
	private int fileCount;

	public String getPath() {
		return path;
	}

	public void setPath(String directory) {
		this.path = directory;
		int lastIndexOf = this.path.lastIndexOf("/") + 1;
		this.name = this.path.substring(lastIndexOf);
	}

	public String getFirstFileName() {
		return firstFileName;
	}

	private void setFirstFileName(String firstFileName) {
		this.firstFileName = firstFileName;
	}

	public String getName() {
		return name;
	}

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
        if (fileNames != null) {
            setFileCount(fileNames.size());
            if (fileNames.size() > 0) {
                for (int i = 0; i < fileCount; ++i) {
                    String fileName = fileNames.get(i);
                    if (!StringUtil.isNull(fileName)) {
                        setFirstFileName(fileName);
                        break;
                    }
                }
            }
        }
    }

    public int getFileCount() {
		return fileCount;
	}

	private void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

}
