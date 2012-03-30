BeeHive
=======

Idea to create simple scheduler, that manage small cluster of machines. Database will be used to coordinate between machines. 
(I now it is SPOF, but in future there is intent to do coordination plugable and use something like zookeeper or dooozer).

Development
-----------

### Languages used
 
Java - sever side
JavaScript - web interface 

### Some info about components 

* [jackson](http://jackson.codehaus.org/Home) - JSON binding and configuration
* [groovy](http://groovy.codehaus.org/) - templates
* [wingstone](http://winstone.sourceforge.net/) - servlet container


### DataModel

[see image](https://github.com/repshak/BeeHive/blob/master/BeeHive/dbModel.png)
