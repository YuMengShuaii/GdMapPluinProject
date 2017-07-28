# GdMapPluinProject


## 前言
      该工具库是基于高德地图封装，简化定位功能，云图搜索，地址查询，线路查询，线路规划，以及地图绘制等。

##  Install
      
###  Maven

		<dependency>
		<groupId>com.enation.geamtear.map</groupId>
		<artifactId>gdmap-utils</artifactId>
		<version>1.0.3</version>
		<type>pom</type>
		</dependency> 
		
###  Gradle 

		compile 'com.enation.geamtear.map:gdmap-utils:1.0.3'
### LVY
		<dependency org='com.enation.geamtear.map' name='gdmap-utils' rev='1.0.3'>
  		<artifact name='gdmap-utils' ext='pom' ></artifact>
		</dependency>
		
##  使用

   **使用前必须有以下配置：**
   
       ApiKey需要到高德开放平台申请  并引入相关权限
           <!-- 用于进行网络定位 -->
		<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
		<!-- 用于访问GPS定位 -->
		<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
		<!-- 获取运营商信息，用于支持提供运营商信息相关的接口 -->
		<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
		<!-- 用于访问wifi网络信息，wifi信息会用于进行网络定位 -->
		<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
		<!-- 这个权限用于获取wifi的获取权限，wifi信息会用来进行网络定位 -->
		<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
		<!-- 用于访问网络，网络定位需要上网 -->
		<uses-permission android:name="android.permission.INTERNET" />
		<!-- 用于读取手机当前的状态 -->
		<uses-permission android:name="android.permission.READ_PHONE_STATE" />
		<!-- 写入扩展存储，向扩展卡写入数据，用于写入缓存定位数据 -->
		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
		<!-- 用于申请调用A-GPS模块 -->
		<uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />
	    <!-- 用于申请获取蓝牙信息进行室内定位 -->
	    <uses-permission android:name="android.permission.BLUETOOTH" />
	    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
	    <!-- 允许程序设置内置sd卡的写权限 -->
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        
		<meta-data android:name="com.amap.api.v2.apikey"
		android:value="xxxxxxxxxxxxxxxxxxxx" />
		<service android:name="com.amap.api.location.APSService" />
		


**获取定位 ：**
			
		/**第一个参数是Context 第二个参数 是否使用连续定位 第三个参数监听事件*/
		Location.getLocation().getLocationDetail(this,true, new Listener.DataLisener<AMapLocation>() {
		            @Override
		            public void success(AMapLocation cloudResult) {
		
		            }
		
		            @Override
		            public void faild(String errorMessage) {
		
		            }
		        });
		       
**获取地图辅助工具：**
        
        /**
         * 获取MapUtils对象
         * @param context        调用者上下文
         * @param aMap           Amap对象
         * @param end            目的地坐标
         * @param isVisableEnd   是否显示InfoWindow
         * @param endTitle       目的地名字
         */ 
		  MapUtils mapUtils = MapUtils.getMapUtils(this, aMap, Application.latlon, (String) Application.get("title",false), true);
		   
	   /**
	     * 更新目的地坐标信息
	     * @param end   结束坐标
	     */
	     mapUtils.setEndLocation(LatLng end);
		
       /**
	     * 绘制坐标信息（起点，目的地，方向标）
	     * @param end 目的地
	     * @param isVisableEnd 是否显示infoWindow
	     */
        mapUtils.setMarker(LatLng end, boolean isVisableEnd)
		
		/**
        * 调用该类结束，释放资源
        */
        mapUtils.finshMapUtils()
      
       /**
        * 移动地图
        * @param start 坐标
        */
        mapUtils.moveMap(LatLonPoint start)
     
       /**
        * 绘制地图轨迹
        * @param mapType 类型
        * @param path    路线信息
        * @param start   开始点
        * @param end     结束点
        * @param infos   地图点集合
        */
        mapUtils.setPathTrajectory(@MapType.MT int mapType, Object path, LatLonPoint start, LatLonPoint end, ArrayList<PathModel.Path.BusStep.Info> infos);
        
 **查询路线：**
       
      /**
     	 * 获取路线信息
       * @param type 类型 BUS:公交车 FOOD：步行 DIRVER：驾车
       * @param isToShop 是否去目的地  为false的话 规划返回路线
       * @param getPath 监听事件
       * @param end     目的地坐标
       */
		GetPathUtils.getPathUtils(this).getPathData(Application.latlon, MapType.BUS, new Listener.DataLisener<PathModel>() {
	        @Override
	        public void success(PathModel cloudResult) {
	            
	        }
	
	        @Override
	        public void faild(String errorMessage) {
	
	        }
	    },false);
	    
**云图服务：**
			  
			首先初始化配置 在Application中
			/**初始化云图表ID*/
			CloudMap.initCloudMap(TABLEID);
			/**初始化云图查询时分页大小*/
			CloudMap.initCloudQueryPageSize(10);
			
			然后调用查询方法
						
			/**
			* 查询商家
			* @param context         上下文
			* @param location        中心坐标
			* @param pageNum         分页数
			* @param condition       关键字
			* @param city            搜索的城市
			* @param findBound       搜索的范围
			* @param filterKey       过滤属性名
			* @param filterValue     过滤属性值
			* @param mapQueryLisener 监听
			*/
			MapQueryUtils.Query(final Context context, AMapLocation location, int pageNum, String condition, String city, int findBound, String filterKey, String filterValue, final Listener.DataLisener<CloudResult> mapQueryLisener);
			
					
			/**
			* 本地查询
			* @param context         上下文
			* @param pageNum         分页页数
			* @param condition       KeyWord
			* @param city            搜索城市
			* @param filterKey       过滤属性名
			* @param filterValue     过滤属性值
			* @param mapQueryLisener 监听
			*/
		 
			MapQueryUtils.QueryLocal(final Context context,int pageNum, String condition,String city,String filterKey,String filterValue,final Listener.DataLisener<CloudResult> mapQueryLisener);
					
			/**
			* 周边查询
			* @param context           上下文
			* @param location          中心坐标
			* @param pageNum           分页页数
			* @param condition         关键字
			* @param findBound         搜搜范围 默认5000米
			* @param filterKey         过滤属性名
			* @param filterValue       过滤属性值
			* @param mapQueryLisener   监听
			*/
			MapQueryUtils.QueryBound(final Context context, AMapLocation location, int pageNum, String condition, int findBound, String filterKey, String filterValue, final Listener.DataLisener<CloudResult> mapQueryLisener);
	
		
