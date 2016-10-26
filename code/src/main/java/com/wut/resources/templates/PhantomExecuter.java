package com.wut.resources.templates;

import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Deprecated
public class PhantomExecuter {
	
	
	public static int execute(String[] arguments, PrintStream pageStream) {
	    Callable<Process> phantomRender = new PhantomRunner(arguments, pageStream);
        
		final ExecutorService service = Executors.newSingleThreadExecutor();

        try
        {
            final Future<Process> f = service.submit(phantomRender);

            Process result = f.get(60*5, TimeUnit.SECONDS);
            
            return result.waitFor();
        }
        catch (final TimeoutException e)
        {
            System.err.println("Calculation took to long");
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            service.shutdown();
        }
        
        return -1;
	}
}
