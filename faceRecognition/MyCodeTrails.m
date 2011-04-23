load Data 
data=DataVector;
   load  GlobalBest  globalBest  finalC
   
   
   data=randn(25000,2);
   
[indx,ctrs]=kmeans(data,8);
idx=indx;

idx=finalC
  hold on 
plot(data(idx==1,1),data(idx==1,2),'r.','MarkerSize',12)
plot(data(idx==2,1),data(idx==2,2),'b.','MarkerSize',12) 
plot(data(idx==3,1),data(idx==3,2),'g.','MarkerSize',12) 
plot(data(idx==4,1),data(idx==4,2),'c.','MarkerSize',12) 
plot(data(idx==5,1),data(idx==5,2),'m.','MarkerSize',12) 
% for di=1:2:20
%  
%     figure
%     hold on 
% plot(data(idx==2,di),data(idx==2,di+1),'b.','MarkerSize',12)
% 
% plot(data(idx==3,di),data(idx==3,di+1),'g.','MarkerSize',12)
% plot(data(idx==4,di),data(idx==4,di+1),'c.','MarkerSize',12)
% plot(data(idx==5,di),data(idx==5,di+1),'m.','MarkerSize',12)
% plot(data(idx==6,di),data(idx==6,di+1),'y.','MarkerSize',12)
% 
% end 

% plot(ctrs(:,1),ctrs(:,2),'MarkerSize',12,'LineWidth',2)
% plot(ctrs(:,1),ctrs(:,2),'MarkerSize',12,'LineWidth',2)
%  
% legend('Cluster 1','Cluster 2','Centroids',  'Location','NW')