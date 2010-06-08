package org.graphdht.openchord;

import sun.security.util.SecurityConstants;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FilePermission;
import java.lang.reflect.Member;
import java.net.InetAddress;
import java.net.SocketPermission;
import java.security.*;
import java.util.PropertyPermission;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/Jun/2010
 * Time: 13:06:55
 * To change this template use File | Settings | File Templates.
 */
public class GraphDHTSecurityManager extends SecurityManager {
    /**
     * This field is <code>true</code> if there is a security check in
     * progress; <code>false</code> otherwise.
     *
     * @deprecated This type of security checking is not recommended.
     *             It is recommended that the <code>checkPermission</code>
     *             call be used instead.
     */
    @Deprecated
    protected boolean inCheck;

    /*
     * Have we been initialized. Effective against finalizer attacks.
     */
    private boolean initialized = false;


    /**
     * returns true if the current context has been granted AllPermission
     */
    private boolean hasAllPermission() {
        return true;
    }

    /**
     * Tests if there is a security check in progress.
     *
     * @return the value of the <code>inCheck</code> field. This field
     *         should contain <code>true</code> if a security check is
     *         in progress,
     *         <code>false</code> otherwise.
     * @see java.lang.SecurityManager#inCheck
     * @deprecated This type of security checking is not recommended.
     *             It is recommended that the <code>checkPermission</code>
     *             call be used instead.
     */
    @Deprecated
    public boolean getInCheck() {
        return inCheck;
    }

    /**
     * Constructs a new <code>SecurityManager</code>.
     * <p/>
     * <p> If there is a security manager already installed, this method first
     * calls the security manager's <code>checkPermission</code> method
     * with the <code>RuntimePermission("createSecurityManager")</code>
     * permission to ensure the calling thread has permission to create a new
     * security manager.
     * This may result in throwing a <code>SecurityException</code>.
     *
     * @throws java.lang.SecurityException if a security manager already
     *                                     exists and its <code>checkPermission</code> method
     *                                     doesn't allow creation of a new security manager.
     * @see java.lang.System#getSecurityManager()
     * @see #checkPermission(java.security.Permission) checkPermission
     * @see java.lang.RuntimePermission
     */
    public GraphDHTSecurityManager() {
        synchronized (SecurityManager.class) {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                // ask the currently installed security manager if we
                // can create a new one.
                sm.checkPermission(new RuntimePermission
                        ("createSecurityManager"));
            }
            initialized = true;
        }
    }

    /**
     * Returns the current execution stack as an array of classes.
     * <p/>
     * The length of the array is the number of methods on the execution
     * stack. The element at index <code>0</code> is the class of the
     * currently executing method, the element at index <code>1</code> is
     * the class of that method's caller, and so on.
     *
     * @return the execution stack.
     */
    protected native Class[] getClassContext();

    /**
     * Returns the class loader of the most recently executing method from
     * a class defined using a non-system class loader. A non-system
     * class loader is defined as being a class loader that is not equal to
     * the system class loader (as returned
     * by {@link ClassLoader#getSystemClassLoader}) or one of its ancestors.
     * <p/>
     * This method will return
     * <code>null</code> in the following three cases:<p>
     * <ol>
     * <li>All methods on the execution stack are from classes
     * defined using the system class loader or one of its ancestors.
     * <p/>
     * <li>All methods on the execution stack up to the first
     * "privileged" caller
     * (see {@link java.security.AccessController#doPrivileged})
     * are from classes
     * defined using the system class loader or one of its ancestors.
     * <p/>
     * <li> A call to <code>checkPermission</code> with
     * <code>java.security.AllPermission</code> does not
     * result in a SecurityException.
     * <p/>
     * </ol>
     *
     * @return the class loader of the most recent occurrence on the stack
     *         of a method from a class defined using a non-system class
     *         loader.
     * @see java.lang.ClassLoader#getSystemClassLoader() getSystemClassLoader
     * @see #checkPermission(java.security.Permission) checkPermission
     * @deprecated This type of security checking is not recommended.
     *             It is recommended that the <code>checkPermission</code>
     *             call be used instead.
     */
    @Deprecated
    protected ClassLoader currentClassLoader() {
        ClassLoader cl = currentClassLoader0();
        if ((cl != null) && hasAllPermission())
            cl = null;
        return cl;
    }

    private native ClassLoader currentClassLoader0();

    /**
     * Returns the class of the most recently executing method from
     * a class defined using a non-system class loader. A non-system
     * class loader is defined as being a class loader that is not equal to
     * the system class loader (as returned
     * by {@link ClassLoader#getSystemClassLoader}) or one of its ancestors.
     * <p/>
     * This method will return
     * <code>null</code> in the following three cases:<p>
     * <ol>
     * <li>All methods on the execution stack are from classes
     * defined using the system class loader or one of its ancestors.
     * <p/>
     * <li>All methods on the execution stack up to the first
     * "privileged" caller
     * (see {@link java.security.AccessController#doPrivileged})
     * are from classes
     * defined using the system class loader or one of its ancestors.
     * <p/>
     * <li> A call to <code>checkPermission</code> with
     * <code>java.security.AllPermission</code> does not
     * result in a SecurityException.
     * <p/>
     * </ol>
     *
     * @return the class  of the most recent occurrence on the stack
     *         of a method from a class defined using a non-system class
     *         loader.
     * @see java.lang.ClassLoader#getSystemClassLoader() getSystemClassLoader
     * @see #checkPermission(java.security.Permission) checkPermission
     * @deprecated This type of security checking is not recommended.
     *             It is recommended that the <code>checkPermission</code>
     *             call be used instead.
     */
    @Deprecated
    protected Class<?> currentLoadedClass() {
        Class c = currentLoadedClass0();
        if ((c != null) && hasAllPermission())
            c = null;
        return c;
    }

    /**
     * Returns the stack depth of the specified class.
     *
     * @param name the fully qualified name of the class to search for.
     * @return the depth on the stack frame of the first occurrence of a
     *         method from a class with the specified name;
     *         <code>-1</code> if such a frame cannot be found.
     * @deprecated This type of security checking is not recommended.
     *             It is recommended that the <code>checkPermission</code>
     *             call be used instead.
     */
    @Deprecated
    protected native int classDepth(String name);

    /**
     * Returns the stack depth of the most recently executing method
     * from a class defined using a non-system class loader.  A non-system
     * class loader is defined as being a class loader that is not equal to
     * the system class loader (as returned
     * by {@link ClassLoader#getSystemClassLoader}) or one of its ancestors.
     * <p/>
     * This method will return
     * -1 in the following three cases:<p>
     * <ol>
     * <li>All methods on the execution stack are from classes
     * defined using the system class loader or one of its ancestors.
     * <p/>
     * <li>All methods on the execution stack up to the first
     * "privileged" caller
     * (see {@link java.security.AccessController#doPrivileged})
     * are from classes
     * defined using the system class loader or one of its ancestors.
     * <p/>
     * <li> A call to <code>checkPermission</code> with
     * <code>java.security.AllPermission</code> does not
     * result in a SecurityException.
     * <p/>
     * </ol>
     *
     * @return the depth on the stack frame of the most recent occurrence of
     *         a method from a class defined using a non-system class loader.
     * @see java.lang.ClassLoader#getSystemClassLoader() getSystemClassLoader
     * @see #checkPermission(java.security.Permission) checkPermission
     * @deprecated This type of security checking is not recommended.
     *             It is recommended that the <code>checkPermission</code>
     *             call be used instead.
     */
    @Deprecated
    protected int classLoaderDepth() {
        int depth = classLoaderDepth0();
        if (depth != -1) {
            if (hasAllPermission())
                depth = -1;
            else
                depth--; // make sure we don't include ourself
        }
        return depth;
    }

    private native int classLoaderDepth0();

    /**
     * Tests if a method from a class with the specified
     * name is on the execution stack.
     *
     * @param name the fully qualified name of the class.
     * @return <code>true</code> if a method from a class with the specified
     *         name is on the execution stack; <code>false</code> otherwise.
     * @deprecated This type of security checking is not recommended.
     *             It is recommended that the <code>checkPermission</code>
     *             call be used instead.
     */
    @Deprecated
    protected boolean inClass(String name) {
        return classDepth(name) >= 0;
    }

    /**
     * Basically, tests if a method from a class defined using a
     * class loader is on the execution stack.
     *
     * @return <code>true</code> if a call to <code>currentClassLoader</code>
     *         has a non-null return value.
     * @see #currentClassLoader() currentClassLoader
     * @deprecated This type of security checking is not recommended.
     *             It is recommended that the <code>checkPermission</code>
     *             call be used instead.
     */
    @Deprecated
    protected boolean inClassLoader() {
        return currentClassLoader() != null;
    }

    /**
     * Creates an object that encapsulates the current execution
     * environment. The result of this method is used, for example, by the
     * three-argument <code>checkConnect</code> method and by the
     * two-argument <code>checkRead</code> method.
     * These methods are needed because a trusted method may be called
     * on to read a file or open a socket on behalf of another method.
     * The trusted method needs to determine if the other (possibly
     * untrusted) method would be allowed to perform the operation on its
     * own.
     * <p> The default implementation of this method is to return
     * an <code>AccessControlContext</code> object.
     *
     * @return an implementation-dependent object that encapsulates
     *         sufficient information about the current execution environment
     *         to perform some security checks later.
     * @see java.lang.SecurityManager#checkConnect(java.lang.String, int,
     *      java.lang.Object) checkConnect
     * @see java.lang.SecurityManager#checkRead(java.lang.String,
     *      java.lang.Object) checkRead
     * @see java.security.AccessControlContext AccessControlContext
     */
    public Object getSecurityContext() {
        return AccessController.getContext();
    }

    /**
     * Throws a <code>SecurityException</code> if the requested
     * access, specified by the given permission, is not permitted based
     * on the security policy currently in effect.
     * <p/>
     * This method calls <code>AccessController.checkPermission</code>
     * with the given permission.
     *
     * @param perm the requested permission.
     * @throws SecurityException    if access is not permitted based on
     *                              the current security policy.
     * @throws NullPointerException if the permission argument is
     *                              <code>null</code>.
     * @since 1.2
     */
    public void checkPermission(Permission perm) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * specified security context is denied access to the resource
     * specified by the given permission.
     * The context must be a security
     * context returned by a previous call to
     * <code>getSecurityContext</code> and the access control
     * decision is based upon the configured security policy for
     * that security context.
     * <p/>
     * If <code>context</code> is an instance of
     * <code>AccessControlContext</code> then the
     * <code>AccessControlContext.checkPermission</code> method is
     * invoked with the specified permission.
     * <p/>
     * If <code>context</code> is not an instance of
     * <code>AccessControlContext</code> then a
     * <code>SecurityException</code> is thrown.
     *
     * @param perm    the specified permission
     * @param context a system-dependent security context.
     * @throws SecurityException    if the specified security context
     *                              is not an instance of <code>AccessControlContext</code>
     *                              (e.g., is <code>null</code>), or is denied access to the
     *                              resource specified by the given permission.
     * @throws NullPointerException if the permission argument is
     *                              <code>null</code>.
     * @see java.lang.SecurityManager#getSecurityContext()
     * @see java.security.AccessControlContext#checkPermission(java.security.Permission)
     * @since 1.2
     */
    public void checkPermission(Permission perm, Object context) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to create a new class loader.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>RuntimePermission("createClassLoader")</code>
     * permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkCreateClassLoader</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @throws SecurityException if the calling thread does not
     *                           have permission
     *                           to create a new class loader.
     * @see java.lang.ClassLoader#ClassLoader()
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkCreateClassLoader() {
        return;
    }

    /**
     * reference to the root thread group, used for the checkAccess
     * methods.
     */

    private static ThreadGroup rootGroup = getRootGroup();

    private static ThreadGroup getRootGroup() {
        ThreadGroup root = Thread.currentThread().getThreadGroup();
        while (root.getParent() != null) {
            root = root.getParent();
        }
        return root;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to modify the thread argument.
     * <p/>
     * This method is invoked for the current security manager by the
     * <code>stop</code>, <code>suspend</code>, <code>resume</code>,
     * <code>setPriority</code>, <code>setName</code>, and
     * <code>setDaemon</code> methods of class <code>Thread</code>.
     * <p/>
     * If the thread argument is a system thread (belongs to
     * the thread group with a <code>null</code> parent) then
     * this method calls <code>checkPermission</code> with the
     * <code>RuntimePermission("modifyThread")</code> permission.
     * If the thread argument is <i>not</i> a system thread,
     * this method just returns silently.
     * <p/>
     * Applications that want a stricter policy should override this
     * method. If this method is overridden, the method that overrides
     * it should additionally check to see if the calling thread has the
     * <code>RuntimePermission("modifyThread")</code> permission, and
     * if so, return silently. This is to ensure that code granted
     * that permission (such as the JDK itself) is allowed to
     * manipulate any thread.
     * <p/>
     * If this method is overridden, then
     * <code>super.checkAccess</code> should
     * be called by the first statement in the overridden method, or the
     * equivalent security check should be placed in the overridden method.
     *
     * @param t the thread to be checked.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to modify the thread.
     * @throws NullPointerException if the thread argument is
     *                              <code>null</code>.
     * @see java.lang.Thread#resume() resume
     * @see java.lang.Thread#setDaemon(boolean) setDaemon
     * @see java.lang.Thread#setName(java.lang.String) setName
     * @see java.lang.Thread#setPriority(int) setPriority
     * @see java.lang.Thread#stop() stop
     * @see java.lang.Thread#suspend() suspend
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkAccess(Thread t) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to modify the thread group argument.
     * <p/>
     * This method is invoked for the current security manager when a
     * new child thread or child thread group is created, and by the
     * <code>setDaemon</code>, <code>setMaxPriority</code>,
     * <code>stop</code>, <code>suspend</code>, <code>resume</code>, and
     * <code>destroy</code> methods of class <code>ThreadGroup</code>.
     * <p/>
     * If the thread group argument is the system thread group (
     * has a <code>null</code> parent) then
     * this method calls <code>checkPermission</code> with the
     * <code>RuntimePermission("modifyThreadGroup")</code> permission.
     * If the thread group argument is <i>not</i> the system thread group,
     * this method just returns silently.
     * <p/>
     * Applications that want a stricter policy should override this
     * method. If this method is overridden, the method that overrides
     * it should additionally check to see if the calling thread has the
     * <code>RuntimePermission("modifyThreadGroup")</code> permission, and
     * if so, return silently. This is to ensure that code granted
     * that permission (such as the JDK itself) is allowed to
     * manipulate any thread.
     * <p/>
     * If this method is overridden, then
     * <code>super.checkAccess</code> should
     * be called by the first statement in the overridden method, or the
     * equivalent security check should be placed in the overridden method.
     *
     * @param g the thread group to be checked.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to modify the thread group.
     * @throws NullPointerException if the thread group argument is
     *                              <code>null</code>.
     * @see java.lang.ThreadGroup#destroy() destroy
     * @see java.lang.ThreadGroup#resume() resume
     * @see java.lang.ThreadGroup#setDaemon(boolean) setDaemon
     * @see java.lang.ThreadGroup#setMaxPriority(int) setMaxPriority
     * @see java.lang.ThreadGroup#stop() stop
     * @see java.lang.ThreadGroup#suspend() suspend
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkAccess(ThreadGroup g) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to cause the Java Virtual Machine to
     * halt with the specified status code.
     * <p/>
     * This method is invoked for the current security manager by the
     * <code>exit</code> method of class <code>Runtime</code>. A status
     * of <code>0</code> indicates success; other values indicate various
     * errors.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>RuntimePermission("exitVM."+status)</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkExit</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param status the exit status.
     * @throws SecurityException if the calling thread does not have
     *                           permission to halt the Java Virtual Machine with
     *                           the specified status.
     * @see java.lang.Runtime#exit(int) exit
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkExit(int status) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to create a subprocess.
     * <p/>
     * This method is invoked for the current security manager by the
     * <code>exec</code> methods of class <code>Runtime</code>.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>FilePermission(cmd,"execute")</code> permission
     * if cmd is an absolute path, otherwise it calls
     * <code>checkPermission</code> with
     * <code>FilePermission("&lt;&lt;ALL FILES&gt;&gt;","execute")</code>.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkExec</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param cmd the specified system command.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to create a subprocess.
     * @throws NullPointerException if the <code>cmd</code> argument is
     *                              <code>null</code>.
     * @see java.lang.Runtime#exec(java.lang.String)
     * @see java.lang.Runtime#exec(java.lang.String, java.lang.String[])
     * @see java.lang.Runtime#exec(java.lang.String[])
     * @see java.lang.Runtime#exec(java.lang.String[], java.lang.String[])
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkExec(String cmd) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to dynamic link the library code
     * specified by the string argument file. The argument is either a
     * simple library name or a complete filename.
     * <p/>
     * This method is invoked for the current security manager by
     * methods <code>load</code> and <code>loadLibrary</code> of class
     * <code>Runtime</code>.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>RuntimePermission("loadLibrary."+lib)</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkLink</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param lib the name of the library.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to dynamically link the library.
     * @throws NullPointerException if the <code>lib</code> argument is
     *                              <code>null</code>.
     * @see java.lang.Runtime#load(java.lang.String)
     * @see java.lang.Runtime#loadLibrary(java.lang.String)
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkLink(String lib) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to read from the specified file
     * descriptor.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>RuntimePermission("readFileDescriptor")</code>
     * permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkRead</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param fd the system-dependent file descriptor.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to access the specified file descriptor.
     * @throws NullPointerException if the file descriptor argument is
     *                              <code>null</code>.
     * @see java.io.FileDescriptor
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkRead(FileDescriptor fd) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to read the file specified by the
     * string argument.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>FilePermission(file,"read")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkRead</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param file the system-dependent file name.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to access the specified file.
     * @throws NullPointerException if the <code>file</code> argument is
     *                              <code>null</code>.
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkRead(String file) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * specified security context is not allowed to read the file
     * specified by the string argument. The context must be a security
     * context returned by a previous call to
     * <code>getSecurityContext</code>.
     * <p> If <code>context</code> is an instance of
     * <code>AccessControlContext</code> then the
     * <code>AccessControlContext.checkPermission</code> method will
     * be invoked with the <code>FilePermission(file,"read")</code> permission.
     * <p> If <code>context</code> is not an instance of
     * <code>AccessControlContext</code> then a
     * <code>SecurityException</code> is thrown.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkRead</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param file    the system-dependent filename.
     * @param context a system-dependent security context.
     * @throws SecurityException    if the specified security context
     *                              is not an instance of <code>AccessControlContext</code>
     *                              (e.g., is <code>null</code>), or does not have permission
     *                              to read the specified file.
     * @throws NullPointerException if the <code>file</code> argument is
     *                              <code>null</code>.
     * @see java.lang.SecurityManager#getSecurityContext()
     * @see java.security.AccessControlContext#checkPermission(java.security.Permission)
     */
    public void checkRead(String file, Object context) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to write to the specified file
     * descriptor.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>RuntimePermission("writeFileDescriptor")</code>
     * permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkWrite</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param fd the system-dependent file descriptor.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to access the specified file descriptor.
     * @throws NullPointerException if the file descriptor argument is
     *                              <code>null</code>.
     * @see java.io.FileDescriptor
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkWrite(FileDescriptor fd) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to write to the file specified by
     * the string argument.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>FilePermission(file,"write")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkWrite</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param file the system-dependent filename.
     * @throws SecurityException    if the calling thread does not
     *                              have permission to access the specified file.
     * @throws NullPointerException if the <code>file</code> argument is
     *                              <code>null</code>.
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkWrite(String file) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to delete the specified file.
     * <p/>
     * This method is invoked for the current security manager by the
     * <code>delete</code> method of class <code>File</code>.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>FilePermission(file,"delete")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkDelete</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param file the system-dependent filename.
     * @throws SecurityException    if the calling thread does not
     *                              have permission to delete the file.
     * @throws NullPointerException if the <code>file</code> argument is
     *                              <code>null</code>.
     * @see java.io.File#delete()
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkDelete(String file) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to open a socket connection to the
     * specified host and port number.
     * <p/>
     * A port number of <code>-1</code> indicates that the calling
     * method is attempting to determine the IP address of the specified
     * host name.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>SocketPermission(host+":"+port,"connect")</code> permission if
     * the port is not equal to -1. If the port is equal to -1, then
     * it calls <code>checkPermission</code> with the
     * <code>SocketPermission(host,"resolve")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkConnect</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param host the host name port to connect to.
     * @param port the protocol port to connect to.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to open a socket connection to the specified
     *                              <code>host</code> and <code>port</code>.
     * @throws NullPointerException if the <code>host</code> argument is
     *                              <code>null</code>.
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkConnect(String host, int port) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * specified security context is not allowed to open a socket
     * connection to the specified host and port number.
     * <p/>
     * A port number of <code>-1</code> indicates that the calling
     * method is attempting to determine the IP address of the specified
     * host name.
     * <p> If <code>context</code> is not an instance of
     * <code>AccessControlContext</code> then a
     * <code>SecurityException</code> is thrown.
     * <p/>
     * Otherwise, the port number is checked. If it is not equal
     * to -1, the <code>context</code>'s <code>checkPermission</code>
     * method is called with a
     * <code>SocketPermission(host+":"+port,"connect")</code> permission.
     * If the port is equal to -1, then
     * the <code>context</code>'s <code>checkPermission</code> method
     * is called with a
     * <code>SocketPermission(host,"resolve")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkConnect</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param host    the host name port to connect to.
     * @param port    the protocol port to connect to.
     * @param context a system-dependent security context.
     * @throws SecurityException    if the specified security context
     *                              is not an instance of <code>AccessControlContext</code>
     *                              (e.g., is <code>null</code>), or does not have permission
     *                              to open a socket connection to the specified
     *                              <code>host</code> and <code>port</code>.
     * @throws NullPointerException if the <code>host</code> argument is
     *                              <code>null</code>.
     * @see java.lang.SecurityManager#getSecurityContext()
     * @see java.security.AccessControlContext#checkPermission(java.security.Permission)
     */
    public void checkConnect(String host, int port, Object context) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to wait for a connection request on
     * the specified local port number.
     * <p/>
     * If port is not 0, this method calls
     * <code>checkPermission</code> with the
     * <code>SocketPermission("localhost:"+port,"listen")</code>.
     * If port is zero, this method calls <code>checkPermission</code>
     * with <code>SocketPermission("localhost:1024-","listen").</code>
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkListen</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param port the local port.
     * @throws SecurityException if the calling thread does not have
     *                           permission to listen on the specified port.
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkListen(int port) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not permitted to accept a socket connection from
     * the specified host and port number.
     * <p/>
     * This method is invoked for the current security manager by the
     * <code>accept</code> method of class <code>ServerSocket</code>.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>SocketPermission(host+":"+port,"accept")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkAccept</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param host the host name of the socket connection.
     * @param port the port number of the socket connection.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to accept the connection.
     * @throws NullPointerException if the <code>host</code> argument is
     *                              <code>null</code>.
     * @see java.net.ServerSocket#accept()
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkAccept(String host, int port) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to use
     * (join/leave/send/receive) IP multicast.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>java.net.SocketPermission(maddr.getHostAddress(),
     * "accept,connect")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkMulticast</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param maddr Internet group address to be used.
     * @throws SecurityException    if the calling thread is not allowed to
     *                              use (join/leave/send/receive) IP multicast.
     * @throws NullPointerException if the address argument is
     *                              <code>null</code>.
     * @see #checkPermission(java.security.Permission) checkPermission
     * @since JDK1.1
     */
    public void checkMulticast(InetAddress maddr) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to use
     * (join/leave/send/receive) IP multicast.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>java.net.SocketPermission(maddr.getHostAddress(),
     * "accept,connect")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkMulticast</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param maddr Internet group address to be used.
     * @param ttl   value in use, if it is multicast send.
     *              Note: this particular implementation does not use the ttl
     *              parameter.
     * @throws SecurityException    if the calling thread is not allowed to
     *                              use (join/leave/send/receive) IP multicast.
     * @throws NullPointerException if the address argument is
     *                              <code>null</code>.
     * @see #checkPermission(java.security.Permission) checkPermission
     * @since JDK1.1
     * @deprecated Use #checkPermission(java.security.Permission) instead
     */
    @Deprecated
    public void checkMulticast(InetAddress maddr, byte ttl) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to access or modify the system
     * properties.
     * <p/>
     * This method is used by the <code>getProperties</code> and
     * <code>setProperties</code> methods of class <code>System</code>.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>PropertyPermission("*", "read,write")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkPropertiesAccess</code>
     * at the point the overridden method would normally throw an
     * exception.
     * <p/>
     *
     * @throws SecurityException if the calling thread does not have
     *                           permission to access or modify the system properties.
     * @see java.lang.System#getProperties()
     * @see java.lang.System#setProperties(java.util.Properties)
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkPropertiesAccess() {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to access the system property with
     * the specified <code>key</code> name.
     * <p/>
     * This method is used by the <code>getProperty</code> method of
     * class <code>System</code>.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>PropertyPermission(key, "read")</code> permission.
     * <p/>
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkPropertyAccess</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param key a system property key.
     * @throws SecurityException        if the calling thread does not have
     *                                  permission to access the specified system property.
     * @throws NullPointerException     if the <code>key</code> argument is
     *                                  <code>null</code>.
     * @throws IllegalArgumentException if <code>key</code> is empty.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkPropertyAccess(String key) {
        return;
    }

    /**
     * Returns <code>false</code> if the calling
     * thread is not trusted to bring up the top-level window indicated
     * by the <code>window</code> argument. In this case, the caller can
     * still decide to show the window, but the window should include
     * some sort of visual warning. If the method returns
     * <code>true</code>, then the window can be shown without any
     * special restrictions.
     * <p/>
     * See class <code>Window</code> for more information on trusted and
     * untrusted windows.
     * <p/>
     * This method calls
     * <code>checkPermission</code> with the
     * <code>AWTPermission("showWindowWithoutWarningBanner")</code> permission,
     * and returns <code>true</code> if a SecurityException is not thrown,
     * otherwise it returns <code>false</code>.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkTopLevelWindow</code>
     * at the point the overridden method would normally return
     * <code>false</code>, and the value of
     * <code>super.checkTopLevelWindow</code> should
     * be returned.
     *
     * @param window the new window that is being created.
     * @return <code>true</code> if the calling thread is trusted to put up
     *         top-level windows; <code>false</code> otherwise.
     * @throws NullPointerException if the <code>window</code> argument is
     *                              <code>null</code>.
     * @see java.awt.Window
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public boolean checkTopLevelWindow(Object window) {
        return true;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to initiate a print job request.
     * <p/>
     * This method calls
     * <code>checkPermission</code> with the
     * <code>RuntimePermission("queuePrintJob")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkPrintJobAccess</code>
     * at the point the overridden method would normally throw an
     * exception.
     * <p/>
     *
     * @throws SecurityException if the calling thread does not have
     *                           permission to initiate a print job request.
     * @see #checkPermission(java.security.Permission) checkPermission
     * @since JDK1.1
     */
    public void checkPrintJobAccess() {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to access the system clipboard.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>AWTPermission("accessClipboard")</code>
     * permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkSystemClipboardAccess</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @throws SecurityException if the calling thread does not have
     *                           permission to access the system clipboard.
     * @see #checkPermission(java.security.Permission) checkPermission
     * @since JDK1.1
     */
    public void checkSystemClipboardAccess() {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to access the AWT event queue.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>AWTPermission("accessEventQueue")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkAwtEventQueueAccess</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @throws SecurityException if the calling thread does not have
     *                           permission to access the AWT event queue.
     * @see #checkPermission(java.security.Permission) checkPermission
     * @since JDK1.1
     */
    public void checkAwtEventQueueAccess() {
        return;
    }

    /*
     * We have an initial invalid bit (initially false) for the class
     * variables which tell if the cache is valid.  If the underlying
     * java.security.Security property changes via setProperty(), the
     * Security class uses reflection to change the variable and thus
     * invalidate the cache.
     *
     * Locking is handled by synchronization to the
     * packageAccessLock/packageDefinitionLock objects.  They are only
     * used in this class.
     *
     * Note that cache invalidation as a result of the property change
     * happens without using these locks, so there may be a delay between
     * when a thread updates the property and when other threads updates
     * the cache.
     */
    private static boolean packageAccessValid = false;
    private static String[] packageAccess;
    private static final Object packageAccessLock = new Object();

    private static boolean packageDefinitionValid = false;
    private static String[] packageDefinition;
    private static final Object packageDefinitionLock = new Object();

    private static String[] getPackages(String p) {
        String packages[] = null;
        if (p != null && !p.equals("")) {
            java.util.StringTokenizer tok =
                    new java.util.StringTokenizer(p, ",");
            int n = tok.countTokens();
            if (n > 0) {
                packages = new String[n];
                int i = 0;
                while (tok.hasMoreElements()) {
                    String s = tok.nextToken().trim();
                    packages[i++] = s;
                }
            }
        }

        if (packages == null)
            packages = new String[0];
        return packages;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to access the package specified by
     * the argument.
     * <p/>
     * This method is used by the <code>loadClass</code> method of class
     * loaders.
     * <p/>
     * This method first gets a list of
     * restricted packages by obtaining a comma-separated list from
     * a call to
     * <code>java.security.Security.getProperty("package.access")</code>,
     * and checks to see if <code>pkg</code> starts with or equals
     * any of the restricted packages. If it does, then
     * <code>checkPermission</code> gets called with the
     * <code>RuntimePermission("accessClassInPackage."+pkg)</code>
     * permission.
     * <p/>
     * If this method is overridden, then
     * <code>super.checkPackageAccess</code> should be called
     * as the first line in the overridden method.
     *
     * @param pkg the package name.
     * @throws SecurityException    if the calling thread does not have
     *                              permission to access the specified package.
     * @throws NullPointerException if the package name argument is
     *                              <code>null</code>.
     * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
     *      loadClass
     * @see java.security.Security#getProperty getProperty
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkPackageAccess(String pkg) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to define classes in the package
     * specified by the argument.
     * <p/>
     * This method is used by the <code>loadClass</code> method of some
     * class loaders.
     * <p/>
     * This method first gets a list of restricted packages by
     * obtaining a comma-separated list from a call to
     * <code>java.security.Security.getProperty("package.definition")</code>,
     * and checks to see if <code>pkg</code> starts with or equals
     * any of the restricted packages. If it does, then
     * <code>checkPermission</code> gets called with the
     * <code>RuntimePermission("defineClassInPackage."+pkg)</code>
     * permission.
     * <p/>
     * If this method is overridden, then
     * <code>super.checkPackageDefinition</code> should be called
     * as the first line in the overridden method.
     *
     * @param pkg the package name.
     * @throws SecurityException if the calling thread does not have
     *                           permission to define classes in the specified package.
     * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
     * @see java.security.Security#getProperty getProperty
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkPackageDefinition(String pkg) {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to set the socket factory used by
     * <code>ServerSocket</code> or <code>Socket</code>, or the stream
     * handler factory used by <code>URL</code>.
     * <p/>
     * This method calls <code>checkPermission</code> with the
     * <code>RuntimePermission("setFactory")</code> permission.
     * <p/>
     * If you override this method, then you should make a call to
     * <code>super.checkSetFactory</code>
     * at the point the overridden method would normally throw an
     * exception.
     * <p/>
     *
     * @throws SecurityException if the calling thread does not have
     *                           permission to specify a socket factory or a stream
     *                           handler factory.
     * @see java.net.ServerSocket#setSocketFactory(java.net.SocketImplFactory) setSocketFactory
     * @see java.net.Socket#setSocketImplFactory(java.net.SocketImplFactory) setSocketImplFactory
     * @see java.net.URL#setURLStreamHandlerFactory(java.net.URLStreamHandlerFactory) setURLStreamHandlerFactory
     * @see #checkPermission(java.security.Permission) checkPermission
     */
    public void checkSetFactory() {
        return;
    }

    /**
     * Throws a <code>SecurityException</code> if the
     * calling thread is not allowed to access members.
     * <p/>
     * The default policy is to allow access to PUBLIC members, as well
     * as access to classes that have the same class loader as the caller.
     * In all other cases, this method calls <code>checkPermission</code>
     * with the <code>RuntimePermission("accessDeclaredMembers")
     * </code> permission.
     * <p/>
     * If this method is overridden, then a call to
     * <code>super.checkMemberAccess</code> cannot be made,
     * as the default implementation of <code>checkMemberAccess</code>
     * relies on the code being checked being at a stack depth of
     * 4.
     *
     * @param clazz the class that reflection is to be performed on.
     * @param which type of access, PUBLIC or DECLARED.
     * @throws SecurityException    if the caller does not have
     *                              permission to access members.
     * @throws NullPointerException if the <code>clazz</code> argument is
     *                              <code>null</code>.
     * @see java.lang.reflect.Member
     * @see #checkPermission(java.security.Permission) checkPermission
     * @since JDK1.1
     */
    public void checkMemberAccess(Class<?> clazz, int which) {
        return;
    }

    /**
     * Determines whether the permission with the specified permission target
     * name should be granted or denied.
     * <p/>
     * <p> If the requested permission is allowed, this method returns
     * quietly. If denied, a SecurityException is raised.
     * <p/>
     * <p> This method creates a <code>SecurityPermission</code> object for
     * the given permission target name and calls <code>checkPermission</code>
     * with it.
     * <p/>
     * <p> See the documentation for
     * <code>{@link java.security.SecurityPermission}</code> for
     * a list of possible permission target names.
     * <p/>
     * <p> If you override this method, then you should make a call to
     * <code>super.checkSecurityAccess</code>
     * at the point the overridden method would normally throw an
     * exception.
     *
     * @param target the target name of the <code>SecurityPermission</code>.
     * @throws SecurityException        if the calling thread does not have
     *                                  permission for the requested access.
     * @throws NullPointerException     if <code>target</code> is null.
     * @throws IllegalArgumentException if <code>target</code> is empty.
     * @see #checkPermission(java.security.Permission) checkPermission
     * @since JDK1.1
     */
    public void checkSecurityAccess(String target) {
        return;
    }

    private native Class currentLoadedClass0();

    /**
     * Returns the thread group into which to instantiate any new
     * thread being created at the time this is being called.
     * By default, it returns the thread group of the current
     * thread. This should be overridden by a specific security
     * manager to return the appropriate thread group.
     *
     * @return ThreadGroup that new threads are instantiated into
     * @see java.lang.ThreadGroup
     * @since JDK1.1
     */
    public ThreadGroup getThreadGroup() {
        return Thread.currentThread().getThreadGroup();
    }

}
