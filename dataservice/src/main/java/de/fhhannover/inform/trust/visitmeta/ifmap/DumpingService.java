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
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeElement;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeRequest;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeUpdate;
import de.fhhannover.inform.trust.visitmeta.dataservice.util.CryptoUtil;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.ConnectionException;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.IfmapConnectionException;

public class DumpingService implements Runnable {


	private static final Logger log = Logger.getLogger(DumpingService.class);

	private static Set<String> activeSubscriptions;

	private SSRC mSsrc;

	private Connection mConnection;

	static {
		Requests.registerRequestHandler(new DumpRequestHandler());
	}

	public DumpingService(Connection connection, SSRC ssrc){
		log.debug("new DumpingService...");

		activeSubscriptions = new HashSet<String>();
		mSsrc = ssrc;
		mConnection = connection;

		log.debug("... new DumpingService");
	}

	@Override
	public void run(){
		log.debug("run()...");

		long time = 0;
		activeSubscriptions.clear();

		while(!Thread.currentThread().isInterrupted()){

			DumpResult dump = null;
			long lastUpdate = 0;

			try {

				dump = dump(null);

			} catch (ConnectionException e) {

				break;
			}

			if(dump != null){
				lastUpdate = Long.parseLong(dump.getLastUpdate());

				if (lastUpdate > time) {

					time = lastUpdate;
					Collection<Identifier> identifier = dump.getIdentifiers();

					if (identifier != null && !identifier.isEmpty()) {

						Set<String> newUuids;

						try {

							newUuids = subscribeUpdateForDumping(identifier, 10);

						} catch (ConnectionException e) {

							break;
						}

						activeSubscriptions.addAll(newUuids);
					}
				}
			}

			try {

				Thread.sleep(10000);

			} catch (InterruptedException e) {

				log.debug(e.getMessage());
				break;
			}
		}
		log.debug("...run()");
	}

	public DumpResult dump(String filter) throws ConnectionException {

		mConnection.isConnectionEstablished();

		Result res = null;
		DumpRequest dreq = new DumpRequestImpl(filter);

		try {

			res = mSsrc.genericRequestWithSessionId(dreq);

		} catch (IfmapErrorResult e) {

			log.error("ErrorCode: " + e.getErrorCode() + " | ErrorString: " + e.getErrorString(), e);

			throw new IfmapConnectionException();

		} catch (IfmapException e) {

			log.error("Description: " + e.getDescription()+ " | ErrorMessage: " + e.getMessage(), e);

			throw new IfmapConnectionException();
		}

		// If we don't get back a DumpResult instance
		if (!(res instanceof DumpResult)){

			IfmapConnectionException e = new IfmapConnectionException();

			log.error(e.getMessage(), e);

			throw e;
		}

		return (DumpResult) res;
	}

	private Set<String> subscribeUpdateForDumping(Collection<Identifier> idents, Integer depth) throws ConnectionException {
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

				log.trace("send subscribe(req)...");
				subscribe(req);
				log.debug(uuids.size() + " new Identifier was subscribe");
			}
		}

		log.debug("...subscribeUpdateForDumping()");
		return uuids;
	}

	public void subscribe(SubscribeRequest request) throws ConnectionException {

		try {

			mSsrc.subscribe(request);

		} catch (IfmapErrorResult e) {

			log.error("ErrorCode: " + e.getErrorCode() + " | ErrorString: " + e.getErrorString(), e);

			throw new IfmapConnectionException();

		} catch (IfmapException e) {

			log.error("Description: " + e.getDescription()+ " | ErrorMessage: " + e.getMessage(), e);

			throw new IfmapConnectionException();
		}

		for(SubscribeElement r: request.getSubscribeElements()){

			activeSubscriptions.add(r.getName());
		}
	}
}
