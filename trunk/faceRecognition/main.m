clear,clc
check_dir=isdir('average database');
%if check_dir == 0
    mkdir('average database')
    mkdir('eigen faces')
    database_directory = 'my database/'; 
    database_dir='color dbase1/';
    %input('input database directory     ','s');
    persons_number = input('input number of persons in database     ');
    person_samples = input('input number of samples per person     ');
    save('average database/fets','database_directory','persons_number','person_samples');
    save('average database/fets1','database_directory','persons_number','person_samples');
    
   % firstpreparing(database_directory,persons_number,person_samples)
 
     secondpreparing(persons_number,person_samples, database_dir)
%else
 %   load('c:/heba/new/average database/fets','database_directory','persons_number','person_samples');
%end
% add(database_directory,persons_number,person_samples)