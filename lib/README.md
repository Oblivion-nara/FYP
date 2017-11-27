Mokapot
=======

Mokapot is an application and library for allowing you to run Java
programs distributed across multiple Java virtual machines (which
would typically run on different physical machines, allowing the
workload of running the program to be shared between those machines).

Typically, a program you write (that you want to distribute) would
depend on Mokapot as a dependency; Mokapot itself would be run (as an
application) on all but one JVM you wanted to use ("servers", because
they do nothing but wait for commands from the client), and your
program itself would run on the remaining JVM (the "client").

All that's needed to take advantage of Mokapot is to initialize a
communicator, e.g.:

    DistributedCommunicator(
        TCPCommunicationAddress.fromInetAddress(
            InetAddress.getLoopbackAddress(), 15238)).start();

to run at least one method remotely (typically the constructor for an
object you want to use on another machine), e.g.:

    List<String> remoteList = DistributedCommunicator.getCommunicator()
        .runRemotely(() -> new ArrayList<>(),
            TCPCommunicationAddress.fromInetAddress(
                InetAddress.getLoopbackAddress(), 15239));

and to shut down the communicator once you're done (i.e. no longer
have any local references to remote objects or vice versa):

    DistributedCommunicator.getCommunicator().stopCommunication();

Everything else is handled transparently by Mokapot; for example, a
method call to an object can just be written as a normal Java method
call, and will work like a normal Java method call regardless of what
system the object's data is stored on.

Note that Mokapot has several limitations that may prevent it working
on arbitrary programs (see the "limitations" section of
[Guide.md](Guide.md)); in most cases, it will be possible to rewrite
the program to comply with the limitations. The majority of things
that Mokapot can't do are things that are bad Java style anyway (such
as accessing a `public` non-`static` property), although a few of the
limitations may arise even with more reasonable code. A companion
program, Millr, will eventually become available to perform this
rewrite automatically.

Building/depending on Mokapot
-----------------------------

See [Install.md](Install.md) for information on how to build Mokapot,
or to add it as a dependency of your build system.

Using Mokapot
-------------

See [Guide.md](Guide.md) for an introduction on how to use Mokapot in
your programs.
