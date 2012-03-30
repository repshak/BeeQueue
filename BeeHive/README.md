BeeHive
=======

Idea to create simple scheduler, that manage cluster of machines. Database will be used to coordinate between machines. 
(I now it is SPOF. It seems like simpliest way iron out web interface for now). In future we will make coordination plugable 
and use something like [zookeeper](http://zookeeper.apache.org/) or [doozerd](https://github.com/ha/doozerd).

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


Development
-----------

[DataModel](https://github.com/repshak/BeeHive/raw/master/BeeHive/dbModel.png)

Downloads
---------

nothing yet