package org.beequeue.buzz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.template.GroovyTemplate;
import org.beequeue.util.BeeException;
import org.beequeue.util.Files;
import org.beequeue.util.Morph;
import org.beequeue.util.ToStringUtil;

import com.fasterxml.jackson.core.type.TypeReference;

public class BuzzRcConfig {
	private static final Morph<File, String> READ_AND_RESOLVE = new Morph<File, String>() {
		@Override
		public String doIt(File input) {
			try {
				String generate = new GroovyTemplate(Files.readAll(input))
					.generate(BeeQueueHome.instance.getVariables());
				System.out.println(generate);
				return generate;
			} catch (IOException e) {
				throw new BeeException(e);
			}
		}
	};

	public BuzzResourceController[] resourceControllers = new BuzzResourceController[0];
	
	public static Map<BuzzPath,BuzzRcConfig> read(File root) {
		File config = new File(root, "WEB-INF/buzz-config.json.gtemplate");
		return ToStringUtil.toObject(config, READ_AND_RESOLVE, new TypeReference< Map<BuzzPath,BuzzRcConfig> >() {} );
	}

	/**
	 * add all RC entries in  beginning of <code>resourceControllers</code> array. 
	 * @param toAdd
	 */
	public void add(BuzzRcConfig toAdd) {
		ArrayList<BuzzResourceController> list = new ArrayList<BuzzResourceController>();
		list.addAll(Arrays.asList(toAdd.resourceControllers));
		list.addAll(Arrays.asList(this.resourceControllers));
		this.resourceControllers = list.toArray(new BuzzResourceController[list.size()]);
	}

}
