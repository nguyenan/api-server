package com.wut.resources.common;

public class OperationIdentifier {
	private final String operationName;
	private final String resourceName;

	public OperationIdentifier(String resourceName, String opName) {
		this.resourceName = resourceName;
		this.operationName = opName;
	}

	

	public String getOperationName() {
		return operationName;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OperationIdentifier))
			return false;
		OperationIdentifier other = (OperationIdentifier) obj;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equalsIgnoreCase(other.operationName))
			return false;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equalsIgnoreCase(other.resourceName))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return resourceName + ":" + operationName;
	}
}
