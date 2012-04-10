package hotbox;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

class MemoryByteCode extends SimpleJavaFileObject 
{   

	private ByteArrayOutputStream byteCode;   

	public MemoryByteCode(String name)
	{       

		super(URI.create("byte:///" + name + ".class"), Kind.CLASS);   

	}   

	public CharSequence getCharContent(boolean ignoreEncodingErrors) 
	{       

		throw new IllegalStateException();   

	}   

	public OutputStream openOutputStream()
	{       

		byteCode = new ByteArrayOutputStream();       

		return byteCode;   

	}   

	public InputStream openInputStream() 
	{       

		throw new IllegalStateException();   

	}   

	public byte[] getBytes()
	{       

		return byteCode.toByteArray();   

	}
}
