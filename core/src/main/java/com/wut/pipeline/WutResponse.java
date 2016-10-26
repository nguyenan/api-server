package com.wut.pipeline;

//import com.wut.datasources.cache.WutEhCache;
//import com.wut.format.FormatFactory;
import com.wut.format.Formatter;
import com.wut.model.Data;
//import com.wut.protocols.rest.representations.WutRepresentation;
import com.wut.support.ErrorHandler;
import com.wut.support.StreamWriter;

public class WutResponse implements WutResponseInterface {
	private Data d;
	private Formatter f;
	private boolean isError = false;
	private StreamWriter output;
	private boolean isFinalized = false;
	
	public void setError(Data error) {
		if (!isFinalized) {
			isError = true;
			d = error;
			finalize();
		} else {
			ErrorHandler.systemError("response already finalized");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wut.pipeline.WutResponseInterface#getError()
	 */
	@Override
	public Data getError() {
		return isError ? d : null;
	}
	
	/* (non-Javadoc)
	 * @see com.wut.pipeline.WutResponseInterface#represent(com.wut.support.StreamWriter, com.wut.pipeline.WutRequest)
	 */
	//@Override
//	public void represent(StreamWriter out, WutRequest request) {
//		try {
//			if (f == null) {
//				f = FormatFactory.getDefaultFormatter();
//				f.format(ErrorData.INVALID_FORMAT, out, request);
//			} else {
//				f.format(d, out, request);
//			}
//		} catch (Exception e) {
//			ErrorHandler.systemError("error processing request", e);
//		}
//	}
	
	// TODO rename method represent()
//	public WutRepresentation representResponse(WutRequest request) {
//		//String uri = request.getMetric("uri");
//		//System.out.println(uri);
//		
//		try {
//			if (f == null) {
//				f = FormatFactory.getDefaultFormatter();
//				return new WutRepresentation(f, ErrorData.INVALID_FORMAT, request);
//			} else {
//				return new WutRepresentation(f, d, request);
//			}
//		} catch (Exception e) {
//			ErrorHandler.systemError("error processing request", e);
//		}
//		return null;
//	}


	public void setData(Data data) {
		if (!isFinalized) {
			d = data;
			finalize();
		} else {
			ErrorHandler.systemError("response already finalized");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.wut.pipeline.WutResponseInterface#getData()
	 */
	@Override
	public Data getData() {
		return d;
	}

	public void setFormat(Formatter f2) {
		f = f2;
	}

	public void setStream(StreamWriter stream) {
		this.output = stream;
	}
	
	/* (non-Javadoc)
	 * @see com.wut.pipeline.WutResponseInterface#getStream()
	 */
	@Override
	public StreamWriter getStream() {
		return this.output;
	}
	
	public void finalize() {
		isFinalized = true;
	}
	
	/* (non-Javadoc)
	 * @see com.wut.pipeline.WutResponseInterface#isFinalized()
	 */
	@Override
	public boolean isFinalized() {
		return isFinalized;
	}

	/* (non-Javadoc)
	 * @see com.wut.pipeline.WutResponseInterface#getFormat()
	 */
	@Override
	public Formatter getFormat() {
		return f;
	}

	// TODO Add ability to finalize(), which means nobody else should change the response. Pipeline should check after each "processor" (rename processors to pipes?)
	
	
}
