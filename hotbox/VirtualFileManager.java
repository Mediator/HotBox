package hotbox;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

public class VirtualFileManager extends ForwardingJavaFileManager {
	protected MemoryByteCode mbc;
	protected VirtualFileManager(JavaFileManager arg0, MemoryByteCode mbc) {
		super(arg0);
		this.mbc = mbc;
		
	}
	
	 public JavaFileObject getJavaFileForOutput(Location location, String name, JavaFileObject.Kind kind, FileObject sibling) throws IOException 
	 {            
		         return mbc;   
	 }


}
