package com.example.lib_view_inject_processor;

import com.example.lib_inject_view_annotation.MyBindView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class BindingProcessor extends AbstractProcessor {

    private Filer filer;

    /**
     * 获取编写Java文件的Filer对象
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        filer = processingEnvironment.getFiler();
    }

    /**
     * 创建XXXBinding.java文件
     * <p>
     * XXXActivity是根元素
     * 内部的成员变量、方法和内部类都是一级子元素，所以需要遍历，筛选出我们需要的成员变量
     *
     * @param set
     * @param roundEnvironment 环境
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        System.out.println("running!!!!!!1");
        for (Element element : roundEnvironment.getRootElements()) { // 获取根元素，对于当前情况，XXXActivity就是根元素
            String packageStr = element.getEnclosingElement().toString(); // 获取Activity内部的首元素 - 包信息
            String classStr = element.getSimpleName().toString();
            ClassName className = ClassName.get(packageStr, classStr + "$Binding");

            // -------------------------------构建构造函数-----------------------------------
            MethodSpec.Builder constructorBuilder = MethodSpec
                    .constructorBuilder()
                    .addModifiers(Modifier.PUBLIC) // 方法的访问权限
                    .addParameter(ClassName.get(packageStr, classStr), "activity"); // 方法的参数类型和参数名

            boolean hasBinding = false;

            for (Element enclosedElement : element.getEnclosedElements()) { // 对每个子元素进行遍历
                MyBindView bindView = enclosedElement.getAnnotation(MyBindView.class);
                if (bindView != null) {
                    hasBinding = true;

                    // --------------------------------构造函数中添加函数体----------------------------------
                    constructorBuilder.addStatement("activity.$N = activity.findViewById($L)",
                            enclosedElement.getSimpleName(), bindView.value());
                }
                /*if (enclosedElement.getKind() == ElementKind.FIELD) {

                }*/
            }

            // --------------------构建类：访问权限、构造函数-----------------
            TypeSpec builtClass = TypeSpec
                    .classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)  // 给类添加访问权限修饰符
                    .addMethod(constructorBuilder.build()) // 给类添加方法
                    .build();

            // --------------------------创建Java文件-----------------------
            if (hasBinding) {
                try {
                    JavaFile.builder(packageStr, builtClass)
                            .build()
                            .writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 告诉程序，该Processor支持哪些注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(MyBindView.class.getCanonicalName());
    }
}
