package de.fhhannover.inform.trust.visitmeta.ifmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.ifmapj.channel.SSRC;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapException;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifier;
import de.fhhannover.inform.trust.ifmapj.messages.Requests;
import de.fhhannover.inform.trust.ifmapj.messages.Result;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeRequest;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeUpdate;
import de.fhhannover.inform.trust.visitmeta.dataservice.util.CryptoUtil;

public class DumpingService implements Runnable {

	private static final Logger log = Logger.getLogger(UpdateService.class);

	private SSRC mSsrc;

	private static Set<String> activeSubscriptions = new HashSet<String>();
	
	static {
		Requests.registerRequestHandler(new DumpRequestHandler());
	}
	
	public DumpingService(SSRC ssrc){
		log.debug("new DumpingService...");
		mSsrc = ssrc;
	}

	@Override
	public void run(){
		Thread.currentThread().setName("DumpingThread");
		log.debug("run()...");
		long time = 0;
		
		while(!Thread.currentThread().isInterrupted()){

				DumpResult dump = null;
				try {
					dump = dump(null);
				} catch (IfmapException e) {
					log.error(e.getDescription(), e);
				} catch (IfmapErrorResult e) {
					log.error(e.getErrorString(), e);
				}

				long lastUpdate = 0;
				if(dump != null){
					lastUpdate = Long.parseLong(dump.getLastUpdate());
				}
				
				if (lastUpdate > time) {

					time = lastUpdate;
					Collection<Identifier> identifier = dump.getIdentifiers();

					if (identifier != null && !identifier.isEmpty()) {

						Set<String> newUuids = subscribeUpdateForDumping(identifier, 10);

						activeSubscriptions.addAll(newUuids);
					}
				}

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					log.debug(e.getMessage());
					break;
				}
		}
		log.debug("...run() Dumping stopped!");
	}

	private Set<String> subscribeUpdateForDumping(Collection<Identifier> idents, Integer depth) {
		log.debug("subscribeUpdateForDumping()...");
		Set<String> uuids = new HashSet<String>();

		if (idents != null && !idents.isEmpty()) {
			Iterator<Identifier> iter = idents.iterator();
			SubscribeRequest req = null;

			while (iter.hasNext()) {
				SubscribeUpdate update = Requests.createSubscribeUpdate();
				Identifier ident = iter.next();

				String uuid = CryptoUtil.generateMD5BySize(ident.toString(), 16);
				update.setName(uuid);

				update.setStartIdentifier(ident);

				if (depth != null && depth > 0){
					update.setMaxDepth(depth);
				}

				if(!activeSubscriptions.contains(uuid) && !uuids.contains(uuid)){
					if(req == null){
						req = Requests.createSubscribeReq();
					}
					req.addSubscribeElement(update);
					uuids.add(uuid);
				}

			}
			
			if(req != null){
				try {
					log.debug("send subscribe(req)...");
					mSsrc.subscribe(req);
					log.debug(uuids.size() + " new Identifier was subscribe");
				} catch (IfmapErrorResult e) {
					log.error(e.getErrorString(), e);
				} catch (IfmapException e) {
					log.error(e.getDescription(), e);
				}
			}
		}
		log.debug("...subscribeUpdateForDumping()");
		return uuids;
	}

	public DumpResult dump(String filter) throws IfmapException, IfmapErrorResult {
		Result res;
		DumpRequest dreq = new DumpRequestImpl(filter);

		// if SSRC not there, do no request!
		if (mSsrc == null)
			return null;

		res = mSsrc.genericRequestWithSessionId(dreq);

		// If we don't get back a DumpResult instance something is messed up...
		if (!(res instanceof DumpResult)){
			throw new IfmapException("DumpRequestHandler:", "dumpRequest didn't result in dumpResult");
		}
		return (DumpResult) res;
	}
	
}
