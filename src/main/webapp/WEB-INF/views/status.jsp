<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="config.jsp"/>
<body>
    <jsp:include page="header.jsp"/>
    <div id="map" style="width:600px;height:500px;"></div>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=d60985de854147ffdf3a155671095138"></script>
	<script>
        let locations = JSON.parse('${jsonLocations}');
		let container = document.getElementById('map');
		let options = {
			center: new kakao.maps.LatLng(37.50264, 126.98046),
			level: 3
		};
		let map = new kakao.maps.Map(container, options);
        locations.forEach((location) => {
            let markerPosition  = new kakao.maps.LatLng(location.latitude, location.longitude); 
            let marker = new kakao.maps.Marker({
                position: markerPosition
            });
            marker.setMap(map);
        });
	</script>
</body>
</html>
