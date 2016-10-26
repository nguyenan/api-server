package com.wut.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WutProcessExecuter {
	
	public static int execute(WutProcess process) {
		final ExecutorService service = Executors.newSingleThreadExecutor();

        try
        {
            final Future<Integer> f = service.submit(process);

            Integer result = f.get(60*5*10, TimeUnit.SECONDS);
            
            return result.intValue();
        }
        catch (final TimeoutException e)
        {
            System.err.println("WutProcess took too long");
            process.kill();
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            service.shutdown();
        }
        
        return -989898989;
	}
	
	
}
