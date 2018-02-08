Using Mokapot
=============

Basic example (client)
----------------------

```java
public class Demo {
    public static void main(String[] args) {
        // Start a communicator on this JVM listening on port 15238.
        DistributedCommunicator communicator
            = DistributedCommunicator(
                    TCPCommunicationAddress.fromInetAddress(
                        InetAddress.getLoopbackAddress(), 15238));
        communicator.start();
        
        // Configure the address of the remote communicator.
        CommunicationAddress remoteAddress
                = TCPCommunicationAddress.fromInetAddress(
                    InetAddress.getLoopbackAddress(), 15239);
        
        // Create a list on the remote machine (remoteList will be a long reference).
        List<String> remoteList = DistributedCommunicator.getCommunicator()
                .runRemotely(() -> new ArrayList<>(), remoteAddress);
        
        // Add an element to the remote list.
        remoteList.add("Some string");
        
        /* Compute the size of the list. The computation will run on
           the remote machine.  Since remoteList is a long reference,
           method invocations will automatically be directed to the
           machine holding the actual object. */
        System.out.println(remoteList.size());
        
        // Shutdown our communicator once we're finished.
        DistributedCommunicator.getCommunicator().stopCommunication();
    }
}
```

Creating a server
-----------------

Mokapot allows a program to run on multiple Java virtual machines at
once. The JVM on which the program starts executing will be one of
them, but you'll also need to start extra JVMs that will also help run
the program. In most cases, these extra JVMs will be "mokapot
servers", i.e. JVMs which *only* respond to remote requests from other
JVMs to help them run a program cooperatively, and do no work of their
own.

The `DistributedServer` class (the default main class for
`mokapot.jar`) has a main method which takes command line arguments
such as the port number and host, e.g.

```
java -Djava.security.manager -Djava.security.policy=localhost-only.policy \
    -cp target/classes:objenesis.jar:javassist.jar:asm-all.jar \
    -jar mokapot.jar 15238 127.0.0.1
```

will start a `DistributedCommunicator` listening on `127.0.0.1:15238`.
Note that the host you give here is your own IP, i.e. the name via
which remote systems will connect to the local system (in this case,
the use of localhost means that the "remote" JVMs must be on the same
physical computer, as otherwise connecting via localhost would not
work).

Other useful command-line options include `-d`, which causes the
server to produce debugging output explaining everything that it's
doing (which could be useful for debugging your application and/or
Mokapot itself, especially when trying to explain performance issues);
and `-w`, which causes the server to exit if it's had no communication
for 40 seconds (useful for preventing server leaks if something goes
wrong when running a Mokepot server in an automated way).

In order for `DistributedCommunicator`s on separate systems to
function properly, they need the same classpath.  This means that when
you run the `DistributedServer` above, you will need your project's
class files on the classpath (in the example above, `target/classes`
refers to your project's class files).

When you run your application, you should give the address of this
server, e.g. `127.0.0.1:15238`, as the address of a remote
`DistributedCommunicator` (see the demo code at the beginning of this
document).

Security Policy
---------------

As the documentation for `DistributedCommunicator` explains,

> This class currently does no sandboxing of its own, so ensure that
> the port is correctly secured via other means, e.g. installing a
> restrictive security manager and firewalling the port. As a
> precaution, attempts to construct instances of the class will fail
> unless a security manager is installed.

You must set the following VM arguments before running the
application, otherwise an exception will be thrown when trying to
start a `DistributedServer` or `DistributedCommunicator`:

```
-Djava.security.manager -Djava.security.policy=/path/to/my-security.policy
```

Your security policy should take into account the fact that anyone who
can access the port on which the server is running can run arbitrary
code on your JVM (and possibly accessing the computer as a whole,
etc.). Unfortunately, Mokapot also needs the ability to read `.class`
files from disk and to override Java's default visibility
restrictions, limiting the extent to which a security policy can help;
I strongly recommend using a restrictive firewall to prevent
unauthorized connections in the first place. Here's an example of a
security policy:

**my-security.policy**
```
grant {
    permission java.net.SocketPermission "127.0.0.1", "connect,resolve,accept,listen";
    permission java.net.SocketPermission "localhost", "connect,resolve,accept,listen";
    permission java.lang.RuntimePermission "accessDeclaredMembers";
    permission java.lang.RuntimePermission "getProtectionDomain";
    permission java.util.PropertyPermission "java.runtime.version", "read";
    permission java.util.PropertyPermission "java.vm.info", "read";
    permission java.util.PropertyPermission "com.google.appengine.runtime.version", "read";
    permission java.lang.RuntimePermission "accessClassInPackage.sun.misc";
    permission java.lang.RuntimePermission "accessClassInPackage.sun.reflect";
    permission java.lang.RuntimePermission "modifyThreadGroup";
    permission java.lang.RuntimePermission "modifyThread";
    permission java.lang.RuntimePermission "reflectionFactoryAccess";
    permission java.lang.reflect.ReflectPermission "suppressAccessChecks";
    permission java.io.FilePermission "<<ALL FILES>>", "read";
};
```

Limitations
-----------

Mokapot cannot safely (i.e. without using the semantics of the
program) be used directly on programs that do any of the following
things:

  * Pass an instance of a `final` class (or effectively `final`,
    e.g. `private`) as a method argument or return value, unless the
    class is `Copiable` or implicitly so;
  * Pass an array as a method argument or return value, unless the
    array is never mutated from that point onwards;
  * Pass a lambda as a method argument or return value, unless the
    lambda is both `Copiable` and `Serializable` (note that you can
    safely replace it with an inner class if you want it to remain
    noncopiable, although lambdas are normally implicitly copiable
    because any captured values must be immutable by definition);
  * Allow a method of one object to access a property of a *different
    object* directly (i.e. without using a getter/setter), unless both
    objects are `Copiable` or implicitly so, or neither object has
    ever been used as a method argument or return value; accessing
    fields of `this` is acceptable, accessing fields of another object
    of the same class or of an inner/outer class is not;
  * Compare objects for reference equality, or objects' actual classes
    for equality, if the objects have ever been used as method
    arguments or return values (you can use the methods in the
    `LengthIndependent` class in order to get around this);
  * Use a `synchronized` *block* on an object that's ever been a
    method argument or return value (a `synchronized` *method* is OK,
    and `synchronized` blocks should be rewritten to use
    `synchronized` methods instead)
  * Access stateful `static` properties or methods (except via
    `runRemotely` specifying the exact machine to run them on, and
    with the understanding that each machine will have its own state)
  * Mutate `enum` constants (note: very few programs ever want to do
    this)

"Method argument or return value" includes both the object that's
actually passed or returned, and (if the object is `Copiable` or
impliclty so) all its fields, recursively until a `NonCopiable` object
is reached.

Rewriting programs into a form that obeys these restrictions can be
done by hand, or by the help of a separate program (Millr).

Copiable and NonCopiable
------------------------

`Copiable` and `NonCopiable` are marker interfaces, which can be used
to tell Mokapot how to distribute objects of a particular type. If you
do not explicitly mark a type as `Copiable` or `NonCopiable`, Mokapot
will use static analysis to determine which type is most
appropriate. If the performance of your application depends on certain
computations being run remotely, then explicitly mark the relevant
types. You can use assertions to verify that an object was indeed
created remotely:

```java
assert DistributionUtils.isStoredRemotely(myObject);
```

### Copiable

If a class/interface is marked as `Copiable`, then the distributed
communication system will always maintain a copy of the object on each
virtual machine that needs it, rather than trying to maintain a
reference to it. This is usually suitable for small, immutable
objects. You should not mark types as `Copiable` if they hold a lot of
data, as the resulting network overhead could be detrimental to the
performance of the application. Mutable types, or types whose
reference identity is relevant (e.g. types whose `.equals` is
reference equality), should also not be marked as `Copiable`, as the
Javadoc explains:

> Copiable: A marker interface specifying that an object can be safely
> replaced with a copy/clone without changing the semantics of the
> program.

If an object is mutable but marked as `Copiable`, and changes are made
to one copy of the object while multiple JVMs have references to it,
then these changes will not be propagated to the other JVMs; this
changes the semantics of the program.

### NonCopiable

This is the opposite of `Copiable`; it ensures the object's data never
exists on multiple systems at once .  For example, assume
`ComputationEngine` implements `NonCopiable`, then the following code
contains a successful assertion:

```java
ComputationEngine remoteComputationEngine 
    = DistributedCommunicator.getCommunicator.runRemotely(
          ComputationEngine::new, remoteAddress);
assert DistributionUtils.isStoredRemotely(remoteComputationEngine);
```

The actual `ComputationEngine` object resides on the remote machine.
Method invocations on the `remoteComputationEngine` object will be
forwarded to the object on the remote machine.

The `Copiable` or `NonCopiable` nature of each of the method's
arguments will determine whether or not Mokapot makes a copy of the
object and forwards it to the remote machine, or whether it uses a
long reference.  You don't need to do anything special here; just make
sure user-defined types are marked as `Copiable` or `NonCopiable` in
cases where this is relevant.

### Walkthrough of Copiable and NonCopiable

To understand this in the context of an example, look at the
`CopiableAndNonCopiableDemo` example in the `examples` directory. We
have three types: `ImmutableType`, which implements `Copiable` (as you
would expect from the name), and `MutableType`, which implements
`NonCopiable`.  Finally, we have another type `Mutator`, which
maintains a reference to an `ImmutableType` and a `MutableType`, and
performs some operations on these objects.  Following the execution of
the program starting at the line

```java
remoteEngine.mutate(immutableType, mutableType);
```

Since `remoteEngine` is a long reference, this method invocation will
actually be performed on the remote machine. Since `ImmutableType` is
`Copiable`, a copy of this object will be passed to the remote
machine. This means that the remote machine will have access to all
of the `ImmutableType`s fields, an won't have to send requests back to
the 'source' machine.  But `MutableType` is `NonCopiable`, which means
that the `MutableType` argument will actually be a long reference.
Method calls will be forwarded to the actual object on the source
machine.

When the `Mutator.mutate(ImmutableType, MutableType)` method is
executed on the remote machine, the value of `val` can be obtained
directly from the `ImmutableType` argument because it is a local copy
to the remote machine. But the value of the `nonPermanentVal` field on
the `MutableType` object cannot be obtained directly, because the
`MutableType` is actually a long reference. Therefore, it must forward
the method invocation to the source machine. When changing the value
of the field by calling `MutableType.setNonPermanentVal(String)`,
again, the call must be forwarded to the source machine.  Since the
argument is a `String`, and `String`s are `Copiable`, the
`nonPermanentVal` field on the `MutableType` object local to the
source machine will be a short reference.

For more information about `Copiable` and `NonCopiable`, please read
the documentation.

Short-Circuiting
----------------

Mokapot allows operations on objects which are referenced on multiple systems to be short-circuited, so that method invocations operate exactly as Java short references. This is not the case with Java's RMI - the [specification](https://docs.oracle.com/javase/8/docs/platform/rmi/spec/rmi-objmodel7.html#a3404) states that

> When passing an exported remote object as a parameter or return value in a remote method call, the stub for that remote object is passed instead.

And when a method is invoked on a stub, several things happen, including marshalling and unmarshalling arguments, networks calls, etc.

For example, below is a representation of a Matrix:

```java
class Matrix {

    int[][] elements;

    Matrix(int rows, int cols) {
        this.elements = new int[rows][cols];
    }

    int get(int i, int j) {
        return elements[i][j];
    }

    void add(Matrix other) {
        for (int i = 0; i < elements.length; ++i) {
            for (int j = 0; j < elements[i].length; ++j) {
                elements[i][j] += other.get(i, j);
            }
        }
    }

    public static void main(String[] args) {
        // Create two Matrix objects on the same remote system
        // ...

        a.add(b);
    }
}
```

If we use Mokapot to create two remote Matrix objects, then when the above code calls `other.get(i, j)`, Mokapot will realise that the `other` object is on the same system, and so no network calls (even through loopback) will occur. Instead, the method call will go locally via Java's short reference mechanisms.

However, if we use RMI, `other` will be a stub, and the call will not be short-circuited, i.e. directly invoked on the actual object, even though it is on the same machine.

Destroying Long References
--------------------------

The `DestroyRemoteReferencesDemo` class demonstrates how to ensure your application terminates. Without the line `remoteObject = null;`, the program would not terminate. In order to properly terminate the `DistributedCommunicator` in your application, you must be able to assign null to all long references.
