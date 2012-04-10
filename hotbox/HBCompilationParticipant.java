package hotbox;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;



public class HBCompilationParticipant extends CompilationParticipant {

	public HBCompilationParticipant()
	{

	}

	@Override
	public boolean isActive(IJavaProject project) {
		return true; // springs into action for all Java projects
	}

	private String getJavaOutputPath(IJavaProject project) 
	{
		String result = "";
		try 
		{
			result = getPathAsString(project.getOutputLocation());
		} 
		catch (JavaModelException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	private String getClasspathInfo(IJavaProject project) 
	{
		String result = getJavaOutputPath(project) + ";";
		try
		{
			IClasspathEntry[] entries = project.getResolvedClasspath(true);
			for (int j = 0; j < entries.length; j++) {
				if (entries[j].getContentKind() == IPackageFragmentRoot.K_BINARY
						&& entries[j].getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					result += entries[j].getPath().toString() + ";";
				} else {
					result += getPathAsString(entries[j].getPath()) + ";";
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getPathAsString(IPath entry) {
		String result = "";
		try {
			IFolder folder = ResourcesPlugin.getWorkspace().getRoot()
					.getFolder(entry);
			result = folder.getLocation().toString();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void reconcile(ReconcileContext context) {
		// super.reconcile(context);
		System.out.println("Starting Compilation");
		CategorizedProblem[] ps;



		if ((ps = context.getProblems(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER)) != null)
		{
			for (CategorizedProblem p : ps)
			{
				if (p.isError())
				{
					System.out.println("Problems detected");
					return;
				}
			}

		}
		String cp = getClasspathInfo(context.getWorkingCopy().getJavaProject());
		String compilationUnit = context.getWorkingCopy().getElementName();
		String compilationContents;
		try {
			compilationContents = context.getWorkingCopy().getBuffer().getContents();
		} catch (JavaModelException e) {
			System.err.println("Working Copy Exception");
			e.printStackTrace();
			return;
		}
		CompilationAssistant CA = new CompilationAssistant(compilationUnit, compilationContents, cp);
		CA.setDispatcher(Activator.getCommunicatorServer());
		Thread compilationExecutor = new Thread(CA);
		compilationExecutor.start();

	}


}