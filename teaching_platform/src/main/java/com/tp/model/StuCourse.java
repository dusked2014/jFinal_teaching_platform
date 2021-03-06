package com.tp.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.tp.clientModel.CourseInfo;
import com.tp.clientModel.StuCourseInfo;

public class StuCourse extends Model<StuCourse> {
	Student student = new Student();
	ClassInfo classInfo = new ClassInfo();
	
	/**
	 * 注册班级（相对应该门课程）: register one class from the course
	 * @param stuId
	 * @param classId
	 * @return
	 */
	public Boolean registerClass(String stuId, int classId, String stu_name){
		int res = isChooseClass(stuId, classId);
		if(res == 0){
			// The student hasn't register the class
			// 由class_id get  --》  course_name
			Record info =  classInfo.getCourseClassName(classId);
			if(info != null){
				String class_name = info.getStr("class_name");
				Timestamp d = new Timestamp(System.currentTimeMillis()); 
				Record stu_course = new Record().set("stu_id", stuId).set("stu_name", stu_name).set("class_id", classId).set("class_name", class_name).set("create_time", d);
				
				try {
					Db.save("stu_course", stu_course);
					return true;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return false;
				}	
			}
			
		}
		return false;		
	}
	
	//判断学生是否已经选择了该门课程的 该班级
	// judge whether the student has chosen the class of the course
	public int isChooseClass(String stuId, int classId){
		String sql = "select id from stu_course where stu_id = ? and class_id = ?";
		Record info = Db.findFirst(sql, stuId, classId); // 主要有一个相同的就不行了
		if(info != null){
			return 1; // the student has chosen the class of the course
		}
		return 0; // 学生还没有注册该课程的对应班级，the student hasn't chosen the class
	}
	
	// 判断学生注册是哪门课程的哪个班级（注册了哪个班级）
	// select all the classes of the student
	/**
	 * 1. select all the classes that the student has choosen
	 * 2. 判断学生注册是哪门课程的哪个班级（注册了哪个班级）
	 * 3. 三表关联：table stu_course, class_info, course
	 * @param stuId
	 * @return
	 */
	public List<StuCourseInfo> getClassByStuId(String stuId){
		List<StuCourseInfo> listData = new ArrayList<StuCourseInfo>();
		String sql2 = "select a.class_id, a.class_name, b.course_id, b.course_name, b.tea_id, b.tea_name from stu_course a, class_info b where a.stu_id = ? and a.class_id = b.class_id order by a.create_time DESC";
		List<Record> classList = Db.find(sql2, stuId);
		if(classList.size()>0){
			String sql = "select task_id, title, content, create_time, end_time from task where class_id = ? order by create_time DESC limit 1";
			for(Record cinfo:classList){
				StuCourseInfo stuCourseInfo = new StuCourseInfo();
				int class_id = cinfo.getInt("class_id");
				
				stuCourseInfo.setClass_id(class_id);
				stuCourseInfo.setClass_name(cinfo.getStr("class_name"));
				stuCourseInfo.setCourse_id(cinfo.getInt("course_id"));
				stuCourseInfo.setCourse_name(cinfo.getStr("course_name"));
				stuCourseInfo.setTea_id(cinfo.getStr("tea_id"));
				stuCourseInfo.setTea_name(cinfo.getStr("tea_name"));
				
				List<Record> taskList = Db.find(sql, class_id);
				if(taskList.size() > 0) {
					stuCourseInfo.setNewest(taskList);
				}
				listData.add(stuCourseInfo);
			}
			return listData;
		}
		return null;
	}
	
	// 查找一个class里面的学生
	public List<Record> getStudentsInClass(int classId) {
		String sql1 = "select a.class_name, a.stu_id, a.stu_name, b.email, b.phone from stu_course a, stu_info b where a.class_id = ? and a.stu_id = b.id";
		List<Record> stuList = Db.find(sql1, classId);
		if(stuList.size() > 0) {
			return stuList;
		}
		return null;
	}

}
