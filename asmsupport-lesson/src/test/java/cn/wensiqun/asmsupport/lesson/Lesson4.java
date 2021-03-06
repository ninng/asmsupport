package cn.wensiqun.asmsupport.lesson;

import cn.wensiqun.asmsupport.client.DummyClass;
import cn.wensiqun.asmsupport.client.block.*;
import cn.wensiqun.asmsupport.client.def.var.FieldVar;
import cn.wensiqun.asmsupport.client.def.var.LocVar;
import cn.wensiqun.asmsupport.org.objectweb.asm.Opcodes;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by woate on 2016/5/16.
 * 这节课讲解如何创建逻辑
 */
public class Lesson4 {
    static final String LESSON = "Lesson4";
    static final String PACKAGE = "lesson4";
    static final String OUTPUT_PATH = "./target/asmsupport-generated";
    /**
     * 创建一个判断输入字符串是否为test,如果为test，输出内容和1，2，3
     */
    @Test
    public void test1()throws Exception {
        DummyClass dc = new DummyClass().package_(PACKAGE).public_().name(LESSON + "test1").setJavaVersion(Opcodes.V1_7).setClassOutPutPath(OUTPUT_PATH);
        dc.newConstructor().public_().body(new ConstructorBody() {
            @Override
            public void body(LocVar... args) {
                return_();
            }
        });
        dc.newMethod("fun").public_().argTypes(String.class).argNames("name").return_(void.class).body(new MethodBody() {
            @Override
            public void body(final LocVar... args) {
                if_(new IF(call(args[0], "equals", val("this"))) {
                    @Override
                    public void body() {
                        FieldVar out = val(System.class).field("out");
                        out.call("println", args[0]);
                        out.call("println",val("1"));
                        out.call("println", val("2"));
                        out.call("println", val("3"));
                    }
                });
                return_();
            }
        });
        Class cls = dc.build();
        //测试使用新创建的类
        try {
            Object obj = cls.newInstance();
            Method fun = cls.getMethod("fun", String.class);
            fun.invoke(obj, "this");
            fun.invoke(obj, "demo");
            fun.invoke(obj, "demo1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个判断输入字符串是否为test,如果为test，输出内容和1，2，3，如果不是，则输出错误
     */
    @Test
    public void test2() throws Exception {
        DummyClass dc = new DummyClass().public_().package_(PACKAGE).name(LESSON + "test2").setJavaVersion(Opcodes.V1_7).setClassOutPutPath(OUTPUT_PATH);
        dc.newConstructor().public_().body(new ConstructorBody() {
            @Override
            public void body(LocVar... args) {
                return_();
            }
        });
        dc.newMethod("fun").public_().argTypes(String.class).argNames("name").return_(void.class).body(new MethodBody() {
            @Override
            public void body(final LocVar... args) {
                if_(new IF(call(args[0], "equals", val("test"))) {
                    @Override
                    public void body() {
                        FieldVar out = val(System.class).field("out");
                        out.call("println", args[0]);
                        out.call("println",val("1"));
                        out.call("println", val("2"));
                        out.call("println", val("3"));
                    }
                }).else_(new Else() {
                    @Override
                    public void body() {
                        FieldVar out = val(System.class).field("out");
                        out.call("println", val("error"));
                    }
                });
                return_();
            }
        });
        Class cls = dc.build();
        //测试使用新创建的类
        try {
            Object obj = cls.newInstance();
            Method fun = cls.getMethod("fun", String.class);
            fun.invoke(obj, "test");
            fun.invoke(obj, "demo");
            fun.invoke(obj, "demo1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建一个判断输入字符串是否为test,如果为test，输出内容和1，2，3，；如果是demo，直接结束函数，如果不是，则输出错误
     */
    @Test
    public void test3(){
        DummyClass dc = new DummyClass().public_().package_(PACKAGE).name(LESSON + "test3").setJavaVersion(Opcodes.V1_7).setClassOutPutPath(OUTPUT_PATH);
        dc.newConstructor().public_().body(new ConstructorBody() {
            @Override
            public void body(LocVar... args) {
                return_();
            }
        });
        dc.newMethod("fun").public_().argTypes(String.class).argNames("name").return_(void.class).body(new MethodBody() {
            @Override
            public void body(final LocVar... args) {
                if_(new IF(call(args[0], "equals", val("test"))) {
                    @Override
                    public void body() {
                        FieldVar out = val(System.class).field("out");
                        out.call("println", args[0]);
                        out.call("println",val("1"));
                        out.call("println", val("2"));
                        out.call("println", val("3"));
                        return_();
                    }
                }).elseif(new ElseIF(call(args[0], "equals", val("demo"))) {
                    @Override
                    public void body() {
                        FieldVar out = val(System.class).field("out");
                        out.call("println", args[0]);
                        return_();
                    }
                }).else_(new Else() {
                    @Override
                    public void body() {
                        FieldVar out = val(System.class).field("out");
                        out.call("println", val("error"));
                        return_();
                    }
                });
                return_();
            }
        });
        Class cls = dc.build();
        //测试使用新创建的类
        try {
            Object obj = cls.newInstance();
            Method fun = cls.getMethod("fun", String.class);
            fun.invoke(obj, "test");
            fun.invoke(obj, "demo");
            fun.invoke(obj, "demo1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 创建一个循环结构输出1-10
     */
    @Test
    public void test4(){
        DummyClass dc = new DummyClass().public_().package_(PACKAGE).name(LESSON + "test4").setJavaVersion(Opcodes.V1_7).setClassOutPutPath(OUTPUT_PATH);
        dc.newConstructor().public_().body(new ConstructorBody() {
            @Override
            public void body(LocVar... args) {
                return_();
            }
        });
        dc.newMethod("fun").public_().argTypes(String.class).argNames("name").return_(void.class).body(new MethodBody() {
            @Override
            public void body(final LocVar... args) {
                final FieldVar out = val(System.class).field("out");
                out.call("println", args[0]);
                final LocVar i = var("i", int.class, val(0));
                //i<= 10;
                //while(i <= 10){...}
                while_(new While(le(i, val(10))) {
                    @Override
                    public void body() {
                        //System.out.println(i)
                        out.call("println", i);
                        //i++;
                        postinc(i);

                    }
                });
                return_();
            }
        });
        Class cls = dc.build();
        //测试使用新创建的类
        try {
            Object obj = cls.newInstance();
            Method fun = cls.getMethod("fun", String.class);
            fun.invoke(obj, "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建一个foreach遍历一个列表,如果列表为null输出this is a null，并且返回，否则遍历列表
     */
    @Test
    public void test5(){
        DummyClass dc = new DummyClass().public_().package_(PACKAGE).name(LESSON + "test5").setJavaVersion(Opcodes.V1_7).setClassOutPutPath(OUTPUT_PATH);
        dc.newConstructor().public_().body(new ConstructorBody() {
            @Override
            public void body(LocVar... args) {
                return_();
            }
        });
        dc.newMethod("fun").public_().argTypes(List.class).argNames("list").return_(void.class).body(new MethodBody() {
            @Override
            public void body(final LocVar... args) {
                if_(new IF(eq(args[0], null_(List.class))) {
                    @Override
                    public void body() {
                        FieldVar out = val(System.class).field("out");
                        out.call("println", val("this is a null"));
                        return_();
                    }
                });
                for_(new ForEach(args[0]) {
                    @Override
                    public void body(LocVar e) {
                        FieldVar out = val(System.class).field("out");
                        out.call("println", e);
                    }
                });
                return_();
            }
        });
        Class cls = dc.build();
        //测试使用新创建的类
        try {
            Object obj = cls.newInstance();
            Method fun = cls.getMethod("fun", List.class);
            List<String> list = Arrays.asList("1","2","3");
            fun.invoke(obj, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
