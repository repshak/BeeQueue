BeeQueue
=======

BeeQueue - is simple event queue and workflow execution engine, it is capable of managing small cluster of machines. Database will be used to coordinate between nodes.  (I know it is SPOF. It seems like simpliest way iron out web interface for now). 

In future I will make  coordination datastore plugable and use something like [zookeeper](http://zookeeper.apache.org/) 
or [doozerd](https://github.com/ha/doozerd).

Current Status
--------------

We are very early in development, so not much is working yet.

### Languages used
 
* Java - sever side
* JavaScript - web interface 

### Some info about components 

* [jackson](http://jackson.codehaus.org/Home) - JSON binding and configuration
* [groovy](http://groovy.codehaus.org/) - templates
* [sigar api](http://support.hyperic.com/display/SIGAR/Home) - OS abstraction 



Development
-----------


[DeveloperSetup](/repshak/BeeQueue/blob/master/design/DeveloperSetup.mediawiki)

Downloads
---------

nothing yet