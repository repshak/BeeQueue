package codegen;


class RemoveTildas {

  static void main(args) {
 	["src","test"].each
 	{
	  new File(it).eachFileRecurse 
	  { 
	    if ( it.name =~ /java~$/ ) {
	      if( it.delete() ){
	      	println it
	      }
	    }
      }
	}
  }

}