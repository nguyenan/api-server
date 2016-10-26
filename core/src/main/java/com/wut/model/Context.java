package com.wut.model;

// TODO get rid of the need for this class
public class Context {
	public enum PositionInfo { FIRST, MIDDLE, LAST }
	public PositionInfo info = PositionInfo.FIRST;
	public int depth = 1;
	public int position = 0;
}
