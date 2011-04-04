for i=0:3
    for j=1:10
        face=imread(strcat('./my database/',num2str(i*10+j),'.0.jpg'));
        subplot(4,10,i*10+j)
        imshow(face)
        title(num2str(i*10+j))
    end
end
% face=imread(strcat('D:\new\our database res\',num2str(1),'.0.jpg'));
% subplot('position',[0 0 .25 .25])
% imshow(face)