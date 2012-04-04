BeeComb
=======

Idea to create simple scheduler, that manage cluster of machines. Database will be used to coordinate between machines. 
(I know it is SPOF. It seems like simpliest way iron out web interface for now). In future we will make 
coordination datastore plugable and use something like [zookeeper](http://zookeeper.apache.org/) 
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
* [wingstone](http://winstone.sourceforge.net/) - servlet container
* [sigar api](http://support.hyperic.com/display/SIGAR/Home) - OS abstraction 


Development
-----------

- [DataModel](https://github.com/repshak/BeeHive/raw/master/BeeHive/design/dbModel.png)
- [ObjectStates](https://github.com/repshak/BeeHive/raw/master/BeeHive/design/ObjectStates.png)

Downloads
---------

nothing yet