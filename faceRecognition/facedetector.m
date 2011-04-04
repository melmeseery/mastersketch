clear,clc
face=imread('.\my database\41.2.jpg');
gray_face=rgb2gray(face);
bw_face=im2bw(face);

face_size=size(face);
for i=1:face_size(1)
    find_first_zero=find(bw_face(i,:)==0,1,'first');
    find_last_zero=find(bw_face(i,:)==0,1,'last');
    if length(find_first_zero)==0
        find_first_zero=face_size(2)+1;
        find_last_zero=face_size(2)-1;
        up_cut=i;
    end
 %   gray_face(i,1:find_first_zero-1)=255;
  %  gray_face(i,find_last_zero+1:face_size(2))=255;
end
gray_face=imcrop(gray_face,[1 up_cut+1 face_size(2)-1 face_size(1)-up_cut-1]);
bw_face=imcrop(bw_face,[1 up_cut+1 face_size(2)-1 face_size(1)-up_cut-1]);
face_size=size(bw_face);
abs_face_half_length=round(face_size(2)/2);
find_zero_before=find(bw_face(:,abs_face_half_length)==0,1,'first');
i=1;
while i<abs_face_half_length
    find_right_zero=find(bw_face(:,abs_face_half_length+i)==0,1,'first');
    if length(find_right_zero)==0
        find_right_zero=face_size(1)+1;
    end
    if abs(find_right_zero-find_zero_before)> 50
        right_cut=abs_face_half_length+i-1;
        i=abs_face_half_length;        
        find_zero_before
    else
        right_cut = face_size(2);
    end
    find_zero_before=find_right_zero;
    i=i+1;
end
find_zero_before=find(bw_face(:,abs_face_half_length)==0,1,'first');
i=1;
while i<abs_face_half_length
    find_left_zero=find(bw_face(:,abs_face_half_length-i)==0,1,'first');
    if length(find_left_zero)==0
        find_left_zero=face_size(1)+1;
    end
    if abs(find_left_zero-find_zero_before)> 50
        left_cut=abs_face_half_length-i+1;
        i=abs_face_half_length;
    else
       left_cut =1; 
    end
    find_zero_before=find_left_zero;
    i=i+1;
end

gray_face=imcrop(gray_face,[left_cut 1 right_cut-left_cut-1 face_size(1)-1]);

imshow(gray_face)