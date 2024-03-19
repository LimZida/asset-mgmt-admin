1. 코드 작성 시 com.mcnc.assetmgmt 하위에 패키지 만들어 진행합니다. 
ex) 로그인 관련 기능은 com.mcmc.assetmgmt.login

2. 특정 기능 내에 controller, dto, entity, repository, service 패키지를 만들어 역할을 관리하고, 
파일 이름은 기능명+역할명 으로 정의합니다. 
ex) com.mcmc.assetmgmt.login.controller 
=> LoginController
com.mcmc.assetmgmt.login.service 
=> LoginService
com.mcmc.assetmgmt.login.entity 
=> LoginEntity
com.mcmc.assetmgmt.login.dto 
=> LoginDto
com.mcmc.assetmgmt.login.repository 
=> LoginRepository

3. 기능들에 꼭 설명과 각주를 달아줍시다.
ex) 
title :
description :
reference :
author :
date :
