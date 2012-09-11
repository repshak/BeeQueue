package org.beequeue.buzz;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=FileSystemBuzzResourceController.class, name="FileSystem"),
    @JsonSubTypes.Type(value=AnyTableResourceController.class, name="AnyTable"),
    @JsonSubTypes.Type(value=QueryResourceController.class, name="Query")
})

public interface BuzzResourceController {
	
	boolean process(BuzzContext ctx) 
			throws IOException;

}
