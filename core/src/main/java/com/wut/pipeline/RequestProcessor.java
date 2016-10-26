package com.wut.pipeline;

import com.wut.model.Data;
import com.wut.model.map.MessageData;
import com.wut.model.message.ErrorData;
import com.wut.resources.ResourceFactory;
import com.wut.resources.common.MissingParameterException;
import com.wut.resources.common.WutOperation;
import com.wut.support.ErrorHandler;

public class RequestProcessor extends AbstractProcessor {
	private ResourceFactory resFactory = ResourceFactory.getInstance();
//	private ResourceFactory resourceFactory = ResourceFactory.getInstance();

	@Override
	public boolean process(WutRequest request, WutResponse response) {
		if (!response.isFinalized()) {
			// TODO not useful any more -- doesnt take effect
			boolean isValidResource = resFactory.checkResourceValid(request);
			if (!isValidResource) {
				
				response.setData(new MessageData("Invalid resource " + request.getResource()
						+ ". Available resources include:"
						+ resFactory.getResources()));
				
				return false;
			}
			
			// still useful
			boolean isValidOperation = resFactory.checkOperationValid(request);
			if (!isValidOperation) {
				response.setData(new MessageData(
						"invalid operation. avaible operation are: "
								+ resFactory.getResource(request.getResource()).getOperations()));
				return false;
			}

			// start timing request
			//String groupPlusName = request.getResource();
			//monitor.start(groupPlusName);

			// 2. process request
			Data data = null;
			try {
				//OperationIdentifier opId = resFactory.getOperationId(request);
				WutOperation op = RequestHelper.getOperation(request);
				data = op.perform(request);
			} catch (MissingParameterException e) {
				response.setData(new ErrorData(e));
				return false;
			} catch (Exception e) {
				final String msg = "failed request due to " + e;
				ErrorHandler.userError(request, msg, e);
//				MessageData err = MessageData.error(e);
//				err.setData(new StringData(msg));
				response.setData(new ErrorData(e)); // TODO do we want this???
				return false;
			} finally {
				//monitor.stop(groupPlusName);
			}

			// Logger logger = Logger.getLogger("com.mycompany");
			
			//return data;
			// location for the perform request functionality?
			response.setData(data);
		}
		return true;
	}

}
