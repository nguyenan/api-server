package com.wut.provider.file;

import com.wut.model.scalar.BooleanData;
import com.wut.model.scalar.IdData;
import com.wut.model.scalar.ScalarData;
import com.wut.model.scalar.StringData;
import com.wut.model.stream.StreamData;
import com.wut.provider.Provider;

public interface SettingProvider extends Provider {

	public ScalarData read(StringData setting);

	public BooleanData update(StringData setting);

	public ScalarData read(IdData customer, StringData setting);

	public BooleanData update(IdData customer, StringData setting);

}
