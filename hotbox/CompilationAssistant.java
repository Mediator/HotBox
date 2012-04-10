package hotbox;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;


public class CompilationAssistant implements Runnable {

	protected String compilationSource;
	protected String compilationUnit;
	protected String classPath;
	protected CommunicatorServer dispatcher;
	public CompilationAssistant(String compilationUnit, String compilationSource, String classPath)
	{
		this.compilationUnit = compilationUnit;
		this.compilationSource = compilationSource;
		this.classPath = classPath;
	}
	
	public void setDispatcher(CommunicatorServer dispatcher)
	{
		this.dispatcher = dispatcher;
	}
	@Override
	public void run() {
	    //JavaCompiler jc = new EclipseCompiler();
		JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
	    MemorySource ms = new MemorySource(compilationUnit,compilationSource);
	    MemoryByteCode mbc = new MemoryByteCode(compilationUnit);
		StandardJavaFileManager fManager = jc.getStandardFileManager(null,null,null);
		//fManager.setLocation(StandardLocation.CLASS_PATH, Collections..( classPath));
		VirtualFileManager vFileManager = new VirtualFileManager(fManager, mbc);
		
		
		
		System.out.println(classPath);
		//TODO pull options from project settings
		List<String> compilationOptions = new ArrayList<String>();
		compilationOptions.add("-classpath");
		compilationOptions.add(classPath);
		List<MemorySource> compilationUnits = Arrays.asList(ms);
		
		
		DiagnosticListener<JavaFileObject> listener = null;
		
		
		
		 Iterable classes = null;           
		 Writer out = null;         
		 JavaCompiler.CompilationTask compilationTask = jc.getTask(out, vFileManager, listener, compilationOptions, classes, compilationUnits);           
		 System.out.println("Compiling");
		 if (!compilationTask.call())
		 {
			 System.out.println("Compilation Failed");
		 }
		 else
		 {
			// System.out.println("Compiled: ");
			//System.out.println( Arrays.toString(mbc.getBytes()));
			 if (dispatcher != null)
			 {
				 dispatcher.publishCodeUpdate(compilationUnit.replace(".java", ""), mbc.getBytes());
			 }
		 }
		
	}

}
