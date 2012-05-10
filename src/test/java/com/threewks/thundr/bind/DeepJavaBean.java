package com.threewks.thundr.bind;

import java.util.List;

public class DeepJavaBean {
	private String name;
	private List<JavaBean> beans;

	public DeepJavaBean() {
	}

	public DeepJavaBean(String name, List<JavaBean> beans) {
		super();
		this.name = name;
		this.beans = beans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<JavaBean> getBeans() {
		return beans;
	}

	public void setBeans(List<JavaBean> beans) {
		this.beans = beans;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beans == null) ? 0 : beans.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeepJavaBean other = (DeepJavaBean) obj;
		if (beans == null) {
			if (other.beans != null)
				return false;
		} else if (!beans.equals(other.beans))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Name: %s Beans: %s", name, beans);
	}

}
