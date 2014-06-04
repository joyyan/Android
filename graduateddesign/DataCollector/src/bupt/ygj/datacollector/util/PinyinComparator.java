package bupt.ygj.datacollector.util;

import java.util.Comparator;

import bupt.ygj.datacollector.data.AddressVO;

public class PinyinComparator implements Comparator<AddressVO> {  
	  
    @Override
	public int compare(AddressVO o1, AddressVO o2) {  
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  
        if (o2.getSortLetters().equals("#")) {  
            return -1;  
        } else if (o1.getSortLetters().equals("#")) {  
            return 1;  
        } else {  
            return o1.getSortLetters().compareTo(o2.getSortLetters());  
        }  
    }  
}  
