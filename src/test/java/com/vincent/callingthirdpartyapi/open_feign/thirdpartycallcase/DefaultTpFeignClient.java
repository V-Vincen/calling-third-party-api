package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author vincent
 */
public class DefaultTpFeignClient {

    @Test
    public void t() {
        List<String> list = new ArrayList<>();
        list.add("苹果");  //向列表中添加数据
        list.add("草莓");  //向列表中添加数据
        list.add("香蕉");  //向列表中添加数据
        String str = list.remove(1);//移除索引位置为1的元素
        System.out.println("我不爱吃的水果是:" + str);

        list.add(1,"vvvvv");

        Iterator it = list.iterator();  //获取集合的Iterator对象
        while (it.hasNext()) {  //遍历Iterator对象
            System.out.println(it.next());  //输出Iterator对象中元素
        }
    }
}













