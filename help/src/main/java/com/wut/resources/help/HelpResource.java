package com.wut.resources.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.wut.model.Data;
import com.wut.model.Parameter;
import com.wut.model.list.ListData;
import com.wut.model.map.MappedData;
import com.wut.model.scalar.BooleanData;
import com.wut.pipeline.WutRequest;
import com.wut.resources.OperationParameter;
import com.wut.resources.ResourceFactory;
import com.wut.resources.common.CrudResource;
import com.wut.resources.common.WutOperation;
import com.wut.resources.common.WutResource;

// should describe all components
// should describe all resources
// should describe all protocols available

public class HelpResource extends CrudResource {
	
	public HelpResource() {
		super("help", null);
	}

	private static final long serialVersionUID = -725833929895893234L;

	@Override
	public String getName() {
		return "help";
	}

	// TODO make help come from a configuration file (.xml)!!!
	@Override
	public Data read(WutRequest ri) {
		List<Data> help = new ArrayList<Data>();

		ResourceFactory factory = ResourceFactory.getInstance();
		Collection<WutResource> allResources = factory.getResources();
		for (WutResource res : allResources) {
			MappedData map = new MappedData();
			
			// basics
			map.put("name", res.getName());
			map.put("revision", res.getRevision());
			
			// operations
			ListData ops = new ListData();
			for (WutOperation o : res.getOperations()) {
				final String name = o.getName();
				MappedData opMap = new MappedData();
				opMap.put("name", name);
				// describe parameters
				ListData parameters = new ListData();
				for (OperationParameter param : o.getParameters()) {
					MappedData paramMap = new MappedData();
					paramMap.put("name", param.getName());
					//final String typeName = param.getType().getSimpleName().replaceAll("Data", "");
					paramMap.put("type", param.getType().getClass().getSimpleName().replaceAll("Data", ""));
					paramMap.put("required", new BooleanData(param.isRequired()));
				}
				opMap.put("parameters", parameters);
				// describe examples
				final List<String> examples = res.getExamples();
				ListData exampleList = new ListData(examples);
				opMap.put("examples", exampleList);
				ops.add(opMap);
			}
			map.put("operations", ops);

			help.add(map);
		}

		return new ListData(help);
	}

}
