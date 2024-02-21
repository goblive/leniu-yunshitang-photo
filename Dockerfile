FROM   adoptopenjdk:8
LABEL authors="admin"

ENTRYPOINT ["top", "-b"]
# 添加jar到镜像并命名为user.jar
ADD leniu-yunshitang-photo-1.0.jar leniu-yunshitang-photo.jar
# # 镜像启动后暴露的端口
EXPOSE 58901
ENV TZ=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone
# # jar运行命令，参数使用逗号隔开
 ENTRYPOINT ["java","-jar","leniu-yunshitang-photo.jar"]

