package ca.usask.cs.srlab.simcad.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import ca.usask.cs.srlab.simcad.DetectionSettings;
import ca.usask.cs.srlab.simcad.model.CloneSet;

public final class ProcessorDisptacher {

	private static final ProcessorDisptacher INSTANCE = new ProcessorDisptacher();
	private Set<IProcessor> processorList;

	private ProcessorDisptacher() {
		processorList = new LinkedHashSet<IProcessor>();
	}

	public static ProcessorDisptacher getInstance() {
		INSTANCE.cleanUp();
		return INSTANCE;
	}

	public ProcessorDisptacher addProcessor(IProcessor rocessor) {
		processorList.add(rocessor);
		return this;
	}

	public ProcessorDisptacher removeProcessor(IProcessor rocessor) {
		processorList.remove(rocessor);
		return this;
	}

	public ProcessorDisptacher cleanUp() {
		processorList.clear();
		return this;
	}

	public Collection<CloneSet> applyOn(Collection<CloneSet> inputCloneSets, DetectionSettings detectionSettings) {
		Iterator<IProcessor> processorIterator = processorList.iterator();
		long startTime = System.currentTimeMillis();
		
		for (; processorIterator.hasNext();) {
			IProcessor processor = processorIterator.next();
			try {
				Collection<CloneSet> outputCloneSets = new ArrayList<CloneSet>();
				processor.process(inputCloneSets, outputCloneSets);

				if (processorIterator.hasNext())
					inputCloneSets = Collections
							.unmodifiableCollection(outputCloneSets);
				else
					return outputCloneSets;

			} catch (Exception e) {
				throw new IllegalStateException(
						"A processing error occured in processor: "
								+ processor.getName(), e);
			}
		}
		long endTime = System.currentTimeMillis();
		long postprocessingTime  = endTime - startTime;
		detectionSettings.getDetectionReport().setPostprocessingTime(postprocessingTime);
		return inputCloneSets;
	}

}
