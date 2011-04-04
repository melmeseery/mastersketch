persons_number = 30;
for i=1:persons_number
h = imhist(imread(strcat('./eigen faces1/',num2str(i),'.jpg')));
save(strcat('./histogram/','hist',num2str(i),'.','mat'),'h','-mat');
imhist(imread(strcat('./eigen faces1/',num2str(i),'.jpg')));
saveas(gcf,strcat('./histogram/','hist',num2str(i),'.','fig'),'fig');
close all;
end;