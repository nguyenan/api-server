package com.wut.resources.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.wut.model.Data;
import com.wut.model.Parameter;
import com.wut.model.map.MessageData;
import com.wut.pipeline.WutRequest;

public abstract class NewResource implements WutResource {

		private List<WutOperation> operations = new ArrayList<WutOperation>();

		public NewResource() {
			initializeOperations();
		}
		
		private void initializeOperations() {
			Method[] methods = this.getClass().getMethods();
			
			for (Method m : methods) {
				Annotation[] annotations = m.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation.getClass() == OperationAnnotation.class) {
						// TODO add the operation here
//						CrudOperation op = new CrudOperation(opName);
//
//						operations.add(op);

					}
				}
			}
		}

		
		public List<Parameter> getCreateParameters() {
			return Collections.emptyList();
		}
		
		public Data create(WutRequest request) throws Exception {
			return MessageData.NOT_IMPLEMENTED;
		}
		
		public List<Parameter> getReadParameters() {
			return Collections.emptyList();
		}

		public Data read(WutRequest request) throws Exception {
			return MessageData.NOT_IMPLEMENTED;
		}
		
		public List<Parameter> getUpdateParameters() {
			return Collections.emptyList();
		}

		public Data update(WutRequest request) throws Exception {
			return MessageData.NOT_IMPLEMENTED;
		}
		
		public List<Parameter> getDeleteParameters() {
			return Collections.emptyList();
		}

		public Data delete(WutRequest request) throws Exception {
			return MessageData.NOT_IMPLEMENTED;
		}

		@Override
		public List<String> getExamples() {
			return Collections.singletonList("no examples for this resource yet");
		}

		@Override
		public String getHelp() {
			return "no help message yet";
		}

		@Override
		public String getName() {
			String className = getClass().getSimpleName();
			String shortenClassName = className.replaceAll("Resource", "");
			StringBuilder name = new StringBuilder(className.length() + 3);

			for (int i = 0; i < shortenClassName.length(); i++) {
				String c = new String(shortenClassName.charAt(i) + "");
				if (i != 0 && c.equals(c.toUpperCase())) {
					name.append(".");
				}
				name.append(c.toLowerCase());
			}
			// System.out.println("RESOURCE NAME:" + name);
			return name.toString();
		}

//		private class CrudOperation extends WutOperation {
//			private final String name = null;
//
////			public CrudOperation(String name) {
////				this.name = name;
////			}
//
//			@Override
//			public String getName() {
//				return name;
//			}
//			
//			@Override
//			public Data perform(WutRequest ri) throws Exception {
//				if (name.equals("create")) {
//					return create(ri);
//				} else if (name.equals("read")) {
//					return read(ri);
//				} else if (name.equals("update")) {
//					return update(ri);
//				} else if (name.equals("delete")) {
//					return delete(ri);
//				}
//				return MessageData.NOT_IMPLEMENTED;
//			}
//			
//			@Override
//			public List<Parameter> getParameters() {
//				if (name.equals("create")) {
//					return getCreateParameters();
//				} else if (name.equals("read")) {
//					return getReadParameters();
//				} else if (name.equals("update")) {
//					return getUpdateParameters();
//				} else if (name.equals("delete")) {
//					return getDeleteParameters();
//				} else {
//					return Collections.EMPTY_LIST;
//				}
//			}
//
//			@Override
//			public String toString() {
//				return "op:" + NewResource.this.getName() + ":" + getName();
//			}
//		}

		@Override
		public Collection<WutOperation> getOperations() {
			return operations;
		}

		@Override
		public boolean initialize() {
			return true;
		}
		
		public String getRevision() {
			return "1.0";
		}

		@Override
		public String toString() {
			return getName();
		}
	}
