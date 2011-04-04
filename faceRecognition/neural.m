clear,clc
skin=double(imread('D:\new\new\1a.jpg'));
skin_size=size(skin);
re_skin=reshape(skin,skin_size(1)*skin_size(2),3);
re_skin=re_skin';
non_skin=double(imread('D:\new\new\1aa.jpg'));
non_skin_size=size(non_skin);
re_non_skin=reshape(non_skin,non_skin_size(1)*non_skin_size(2),3);
re_non_skin=re_non_skin';
skin_non_skin(:,1:length(re_skin))=re_skin;
skin_non_skin(:,length(re_skin)+1:length(re_skin)+length(re_non_skin))=re_non_skin;
zeros_ones(1:length(re_skin))=1;
zeros_ones(length(re_skin)+1:length(re_skin)+length(re_non_skin))=0;
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
net=newp(minmax(re_skin),1);
net=train(net,skin_non_skin,zeros_ones);
% net=train(net,re_skin,ones(1,length(re_skin)));
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
net_out=sim(net,re_skin);
% skin_2=double(imread('D:\new\new\1aa.jpg'));
% skin_2_size=size(skin_2);
% re_skin_2=reshape(skin_2,skin_2_size(1)*skin_2_size(2),3);
% re_skin_2=re_skin_2';
% net_out=sim(net,re_skin_2);
