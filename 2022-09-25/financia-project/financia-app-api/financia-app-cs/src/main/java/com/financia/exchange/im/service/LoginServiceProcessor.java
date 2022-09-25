/**
 *
 */
package com.financia.exchange.im.service;

import com.financia.common.core.utils.StringUtils;
import com.financia.exchange.Member;
import com.financia.exchange.service.IMemberService;
import org.jim.core.ImChannelContext;
import org.jim.core.cache.redis.RedisCacheManager;
import org.jim.core.packets.*;
import org.jim.core.utils.JsonKit;
import org.jim.server.processor.login.LoginCmdProcessor;
import org.jim.server.protocol.AbstractProtocolCmdProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author WChao
 *
 */
@Component
public class LoginServiceProcessor extends AbstractProtocolCmdProcessor implements LoginCmdProcessor {

	private IMemberService memberService;

	private Logger logger = LoggerFactory.getLogger(LoginServiceProcessor.class);

	public static final Map<String, User> tokenMap = new HashMap<>();

	private static final String SUFFIX = ":";

	public LoginServiceProcessor (IMemberService iMemberService) {
		this.memberService = iMemberService;
	}
	/**
	 * 根据用户名和密码获取用户
	 * @param loginReqBody
	 * @param imChannelContext
	 * @return
	 * @author: WChao
	 */
	public User getUser(LoginReqBody loginReqBody, ImChannelContext imChannelContext) {
		String userId = loginReqBody.getUserId();
		if (StringUtils.isEmpty(userId)) { // 游客
			String deviceId = loginReqBody.getToken();
			User userDevice = getUserDevice(deviceId);
			return userDevice;
		}
		if (userId.equals("admin")) { //客服
			return  getUser();
		}
		 // 会员
		User user = getUser(userId);
		return user;
	}

	public User getUser() {
		User.Builder builder = User.newBuilder()
								   .userId("admin")
								   .nick("客服01号");
		//模拟的群组,正式根据业务去查数据库或者缓存;
//                                   .addGroup();
		//模拟的用户好友,正式根据业务去查数据库或者缓存;
		initFriends(builder);
		builder.avatar(nextImg()).status(UserStatusType.ONLINE.getStatus());
		return builder.build();
	}
	public User getUserDevice(String deviceId) {
//		List<PublicCarouselHome> list = publicCarouselHomeService.list();
		//demo中用map，生产环境需要用cache
		User user = tokenMap.get(deviceId);
		if(Objects.nonNull(user)){
			return user;
		}
		User.Builder builder = User.newBuilder()
								   .userId(deviceId)
								   .nick("游客："+deviceId).sign("visitor");
		//模拟的群组,正式根据业务去查数据库或者缓存;
//                                   .addGroup();
		//模拟的用户好友,正式根据业务去查数据库或者缓存;
//		initFriends(builder);
		builder.avatar(nextImg()).status(UserStatusType.ONLINE.getStatus());
		user = builder.build();
		if (tokenMap.size() > 10000) {
			tokenMap.clear();
		}
		tokenMap.put(deviceId, user);
		return user;
	}
	/**
	 * 根据token获取用户信息
	 * @param userId
	 * @return
	 * @author: WChao
	 */
	public User getUser(String userId) {
//		List<PublicCarouselHome> list = publicCarouselHomeService.list();
		//demo中用map，生产环境需要用cache
		User user = tokenMap.get(userId);
		if(Objects.nonNull(user)){
			return user;
		}
		Member member = memberService.getById(userId);
		User.Builder builder = User.newBuilder()
                                   .userId(member.getId() + "")
                                   .nick(member.getUsername());
                                   //模拟的群组,正式根据业务去查数据库或者缓存;
//                                   .addGroup();
		 //模拟的用户好友,正式根据业务去查数据库或者缓存;
//		initFriends(builder);
		if (StringUtils.isEmpty(member.getAvatar())) {
			builder.avatar(nextImg()).status(UserStatusType.ONLINE.getStatus());
		}else {
			builder.avatar(member.getAvatar()).status(UserStatusType.ONLINE.getStatus());
		}
		user = builder.build();
		if (tokenMap.size() > 10000) {
			tokenMap.clear();
		}
		tokenMap.put(userId, user);
		return user;
	}

	public void initFriends(User.Builder builder){
		Set set = tokenMap.keySet();
		Group myFriendYouke = Group.newBuilder().groupId("1").name("游客").build();
		List<User> myFriendGroupYouke = new ArrayList();
		Group myFriendMember = Group.newBuilder().groupId("2").name("会员").build();
		List<User> myFriendGroupMember = new ArrayList();
		for (Object o : set) {
			User user = tokenMap.get(o);
			String userFriendKey = USER+SUFFIX+"admin"+SUFFIX+user.getUserId();
			List<String> messageList = RedisCacheManager.getCache(PUSH).sortSetGetAll(userFriendKey);
			List<ChatBody> messageDataList = JsonKit.toArray(messageList, ChatBody.class);
			User.Builder builderUser = User.newBuilder();
			if (messageDataList != null && messageDataList.size() > 0){ //获取离线消息数量
				builderUser.addExtra("offLineCount",messageDataList.size());
			}
			User build = builderUser.userId(user.getUserId()).setId(user.getId())
									.nick(user.getNick()).sign(user.getSign()).avatar(user.getAvatar()).build();
			if (!StringUtils.isEmpty(user.getSign()) && user.getSign().equals("visitor")) { //游客
				if (messageDataList != null && messageDataList.size() > 0){
					myFriendGroupYouke.add(0,build);
				}else {
					myFriendGroupYouke.add(build);
				}
			}else {
				if (messageDataList != null && messageDataList.size() > 0){
					myFriendGroupMember.add(0,build);
				}else {
					myFriendGroupMember.add(build);
				}
			}
		}
		myFriendYouke.setUsers(myFriendGroupYouke);
		myFriendMember.setUsers(myFriendGroupMember);
		builder.addFriend(myFriendMember).addFriend(myFriendYouke);
	}

	public String nextImg() {
		return "http://co-base.oss-ap-southeast-1.aliyuncs.com/default/1664027049251_f5eef84f.png";
	}

	/**`
	 * 登陆成功返回状态码:ImStatus.C10007
	 * 登录失败返回状态码:ImStatus.C10008
	 * 注意：只要返回非成功状态码(ImStatus.C10007),其他状态码均为失败,此时用户可以自定义返回状态码，定义返回前端失败信息
	 */
	@Override
	public LoginRespBody doLogin(LoginReqBody loginReqBody, ImChannelContext imChannelContext) {
		if(Objects.nonNull(loginReqBody.getUserId())){
			return LoginRespBody.success();
		}else {
			return LoginRespBody.failed();
		}
	}

	@Override
	public void onSuccess(User user, ImChannelContext channelContext) {
		logger.info("登录成功回调方法");
	}

	@Override
	public void onFailed(ImChannelContext imChannelContext) {
		logger.info("登录失败回调方法");
	}
}
