/**
 *
 */
package com.financia.exchange.im.command;

import com.financia.common.core.utils.StringUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.exception.ImException;
import org.jim.core.http.HttpRequest;
import org.jim.core.packets.Command;
import org.jim.core.packets.LoginReqBody;
import org.jim.core.utils.JsonKit;
import org.jim.server.JimServerAPI;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.LoginReqHandler;
import org.jim.server.processor.handshake.WsHandshakeProcessor;

/**
 * @author WChao
 *
 */
public class CsWsHandshakeProcessor extends WsHandshakeProcessor {

	@Override
	public void onAfterHandshake(ImPacket packet, ImChannelContext imChannelContext) throws ImException {
		LoginReqHandler loginHandler = (LoginReqHandler) CommandManager.getCommand(Command.COMMAND_LOGIN_REQ);
		HttpRequest request = (HttpRequest)packet;
		String username = request.getParams().get("userId") == null ? null : (String)request.getParams().get("userId")[0];
		LoginReqBody loginBody;
		if (StringUtils.isEmpty(username)) {
			String deviceid = request.getParams().get("deviceid") == null ? null : (String)request.getParams().get("deviceid")[0];
			loginBody = new LoginReqBody("", "", deviceid);
		}else {
			loginBody = new LoginReqBody(username, "", "");
		}

		byte[] loginBytes = JsonKit.toJsonBytes(loginBody);
		request.setBody(loginBytes);
		try{
			request.setBodyString(new String(loginBytes, ImConst.CHARSET));
		}catch (Exception e){
			throw new ImException(e);
		}
		ImPacket loginRespPacket = loginHandler.handler(request, imChannelContext);
		if(loginRespPacket != null){
			JimServerAPI.send(imChannelContext, loginRespPacket);
		}
	}
}
