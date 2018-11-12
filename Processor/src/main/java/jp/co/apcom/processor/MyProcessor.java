package jp.co.apcom.processor;

import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("jp.co.apcom.processor.Test")
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {
	@Override
	public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
		List<? extends ClassGenerator<? extends Annotation>> generators = Arrays.asList(new TestGenerator());
		for(ClassGenerator<? extends Annotation> generator : generators) {
			generator.generate(env, processingEnv);
		}
		return true;
	}
}
