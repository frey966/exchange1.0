/**
 *
 */
package com.financia.exchange.im.service;

import com.financia.common.core.utils.StringUtils;
import com.financia.common.redis.service.RedisService;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WChao
 *
 */
@Component
public class LoginServiceProcessor extends AbstractProtocolCmdProcessor implements LoginCmdProcessor {

	private IMemberService memberService;

	private RedisService redisService;

	private Logger logger = LoggerFactory.getLogger(LoginServiceProcessor.class);
	private static final String SUFFIX = ":";
	private static final String REDIS_IM_USER = "redis_im_user";

	public LoginServiceProcessor (IMemberService iMemberService,RedisService redisService) {
		this.memberService = iMemberService;
		this.redisService = redisService;
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

	/**
	 * 客服好友装配
	 * @return
	 */
	public User getUser() {
		User.Builder builder = User.newBuilder()
								   .userId("admin")
								   .nick("客服01号");
		initFriends(builder);
		builder.avatar(nextImgAdmin()).status(UserStatusType.ONLINE.getStatus());
		return builder.build();
	}

	/**
	 * 使用设备id注册im用户
	 * @param deviceId
	 * @return
	 */
	public User getUserDevice(String deviceId) {
		User redisUser = redisService.getCacheObject(REDIS_IM_USER + SUFFIX + deviceId);
		if(Objects.nonNull(redisUser)){
			return redisUser;
		}
		User.Builder builder = User.newBuilder()
								   .userId(deviceId)
								   .nick("游客："+deviceId).sign("visitor");
		builder.avatar(nextImg()).status(UserStatusType.ONLINE.getStatus());
		User user = builder.build();
		redisService.setCacheObject( REDIS_IM_USER + SUFFIX + user.getUserId(),user, 5l, TimeUnit.DAYS);
		return user;
	}

	/**
	 * 使用 会员id注册im用户
	 * @param userId
	 * @return
	 */
	public User getUser(String userId) {
		User redisUser = redisService.getCacheObject(REDIS_IM_USER + SUFFIX + userId);
		Member member = memberService.getById(userId);
		if(Objects.nonNull(redisUser)){
			if (StringUtils.isEmpty(member.getAvatar())) {
				redisUser.setAvatar(nextImg());
			}else {
				redisUser.setAvatar(member.getAvatar());
				redisService.setCacheObject( REDIS_IM_USER + SUFFIX + redisUser.getUserId(),redisUser, 5l, TimeUnit.DAYS);
			}
			return redisUser;
		}
		User.Builder builder = User.newBuilder()
                                   .userId(member.getId() + "")
                                   .nick(member.getUsername());
		if (StringUtils.isEmpty(member.getAvatar())) {
			builder.avatar(nextImg()).status(UserStatusType.ONLINE.getStatus());
		}else {
			builder.avatar(member.getAvatar()).status(UserStatusType.ONLINE.getStatus());
		}
		User user = builder.build();
		redisService.setCacheObject( REDIS_IM_USER + SUFFIX + user.getUserId(),user, 5l, TimeUnit.DAYS);
		return user;
	}

	/**
	 * 客服登陆时装配客服好友列表
	 * 包括离线消息条数，和最后一条聊天记录
	 * @param builder
	 */
	public void initFriends(User.Builder builder){
		// 批量查询登陆的用户
		RedisTemplate redisTemplate = redisService.getRedisTemplate();
		Set<String> keys = redisTemplate.keys(REDIS_IM_USER + SUFFIX + "*");
		List<User> userList = redisTemplate.opsForValue().multiGet(keys);
		// 过滤createTime为空的链接用户
		List<User> usersValid = userList.stream().filter(user -> user.getCreateTime() != null).collect(Collectors.toList());
		// 按照createTime进行排序
		List<User> usersReverse = usersValid.stream().sorted(Comparator.comparing(User::getCreateTime).reversed()).collect(Collectors.toList());
		//初始化 游客分组与会员分组
		Group myFriendYouke = Group.newBuilder().groupId("1").name("游客").build();
		List<User> myFriendGroupYouke = new ArrayList();
		Group myFriendMember = Group.newBuilder().groupId("2").name("会员").build();
		List<User> myFriendGroupMember = new ArrayList();
		//循环 所有用户查询出离线消息条数
		for (User user : usersReverse) {
			System.out.println(user.getCreateTime());
			String userFriendKey = USER+SUFFIX+"admin"+SUFFIX+user.getUserId();
			List<String> messageList = RedisCacheManager.getCache(PUSH).sortSetGetAll(userFriendKey);
			List<ChatBody> messageDataList = JsonKit.toArray(messageList, ChatBody.class);
			User.Builder builderUser = User.newBuilder();
			if (messageDataList != null && messageDataList.size() > 0){ //获取离线消息数量
				builderUser.addExtra("offLineCount",messageDataList.size());
			}
			if (user.getExtras() != null && user.getExtras().get("lastChat") != null && !StringUtils.isEmpty(user.getExtras().get("lastChat").toString())){
				builderUser.addExtra("lastChat",user.getExtras().get("lastChat").toString());
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

	/**
	 * 客服默认头像
	 * @return
	 */
	public String nextImgAdmin() {
		return "https://co-base.oss-ap-southeast-1.aliyuncs.com/default/1664346739021_04810d17.png";
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
