package com.lenovo.lenovorobot_new.bin;

import java.util.ArrayList;

/**
 * 用来封装人的 姓名 和 di,做人脸识别的时候会使用到
 * 
 * @author Administrator
 * 
 */
public class PersonBin {
	public ArrayList<Person> peopleList;

	public class Person {
		public String name;

		public int peopleId;
	}
}
