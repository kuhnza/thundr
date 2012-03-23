package com.atomicleopard.webFramework.bind2;
import java.util.List;

public class DeepPojo {
	private List<Pojo> pojos;

	public DeepPojo(List<Pojo> pojos) {
		super();
		this.pojos = pojos;
	}

	public List<Pojo> getPojos() {
		return pojos;
	}
}
