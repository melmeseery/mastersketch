function firstpreparing(database_directory,persons_number,person_samples)
mkdir('color dbase1')
for i=1:persons_number
    for j=0:person_samples
%   face=imread('d:\heba\facerecog\my database\x.jpg');
    face = imread(strcat(database_directory,num2str(i),'.',num2str(j),'.jpg'));
    face_size = size(face);
%    imshow(face);
  %  face1 = rgb2ycbcr(face)
   face1 = face;
   for k=1:face_size(1) 
        for l=1:face_size(2)
               R = face1(k,l,1) ;
               G = face1(k,l,2);
               B = face1(k,l,3) ;
               
  % R G and B values are presented in the range 0 to 255
  if(R+B+G) ~= 0
      x = double (B)/double((R+G+B));
      y = double (G)/double((R+G+B));
if(( B > 160 && R < 180 && G < 180)||(G > 160 && R < 180 && B < 180)||(G > 150 && B < 90 )||(B < 100 && R < 100 && G < 100)||(x > 0.40)||(y > 0.40)||(R < 102 && G > 100 && B > 110 && G < 140 && B < 160) || (G > 200) || (R+G > 400))    

    face1(k,l,1) =255;
                face1(k,l,2) =255;
                face1(k,l,3) =255;
end
 % else      face1(k,l,1) =255;
  %          face1(k,l,2) =255;
   %         face1(k,l,3) =255;
            end
            end
        end
   
      %imshow(face1);
    %  medfilt2(face1,[3 3]);
      se = strel('square',10);
%     imshow (face1)
   bw_face = im2bw(face1);
   bw_face = imerode(bw_face,se);
   face_size = size(bw_face);
%  imshow(bw_face);
    cut = 1;
        for  k = 1: face_size(1)/4
            find_black = find(bw_face(k,:) == 0);
            if length(find_black) <= 0 
                %face(k,1:find_black(1)-1,:)=255;
               %face(k,find_black(length(find_black))+1:face_size(2),:)=255;
                face(k,1:face_size(2),:)=255 ;
                cut = k + 1;
            end
        end
       face = imcrop(face,[1 cut face_size(2)-1 face_size(1)-cut]);
       face1 = imcrop(face1,[1 cut face_size(2)-1 face_size(1)-cut]);
       bw_face = imcrop(bw_face,[1 cut face_size(2)-1 face_size(1)-cut]);
 %    imshow (bw_face);
        %%%%%%%%%%%%%%%%
        face_size  = size(bw_face);
   %     face_hist = zeros(face_size(2));
        for ii=1:face_size(2)
            face_hist(ii) = 0;
            for jj=1:face_size(1)/2
                if bw_face(jj,ii)== 0
                    face_hist(ii)=face_hist(ii) + 1;
                end
            end
        end
       %%%%%%%%%%%%%%%%%%%%%
        iii=1;
        jjj= uint16(face_size(2)/2);
        flag = 0;
        cut(1) = 40;
        cut(2) = 40;
        while iii < face_size(2)/2
            if ( face_hist(iii)>= 50 && flag == 0)
                cut(1) = iii ;
             %   iii=face_size(2);
                flag = 1;
        %    else
         %       cut(1) = 40;
            end
            iii = iii + 1;
        end
        cut(2) = 0;
        while (jjj > face_hist(cut(1)) && jjj < face_size(2))
            if (face_hist(jjj)>= 40)
                cut(2) = (jjj);
            %    else
            %    cut(2) = face_size(2) - 40;
            end
                jjj = jjj + 1;
            end
        if (cut(2)== cut(1))
            cut(2) = face_size(2)- 40;
        end
        
        %%%%%%%%%%%%%%%
        face = imcrop(face,[cut(1) 1 cut(2)-cut(1) face_size(1)-1]);
        bw_face = imcrop(bw_face,[cut(1) 1 cut(2)-cut(1) face_size(1)-1]);
       % face1 = imcrop(face1,[cut(1) 1 cut(2)-cut(1) face_size(1)-1]);
        face = imresize(face,[80 60],'bilinear');
        bw_face = imresize(bw_face,[80 60],'bilinear');
        %face1 = imresize(face1,[80 60],'bilinear');
        
 %      imshow (bw_face); 
                
        
     %   face = imresize(face,[80 60],'bilinear');
        imwrite(face,strcat('./color dbase1/',num2str(i),'.',num2str(j),'.jpg'));
    end
end
