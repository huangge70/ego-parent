package com.ego.commons.pojo;

import java.io.Serializable;
import java.util.List;

public class EasyUIDataGrid implements Serializable{
	//��ǰҳ��ʾ����
	private List<?> rows;
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public long getTotle() {
		return totle;
	}
	public void setTotle(long totle) {
		this.totle = totle;
	}
	//������
	private long totle;
}
