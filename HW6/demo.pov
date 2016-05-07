global_settings { assumed_gamma 2.2 }

#include "colors.inc"
#include "golds.inc"
#include "metals.inc"
#include "textures.inc"
#include "woods.inc"

background { color Black }

camera {
    location <0, 3, -25>
    look_at  <0, 3,  3>
    angle 30
}

#declare HoopR = 1;      
#declare BBSize = 6;

#declare HoopTop =
union { 
    torus { HoopR,.1
        pigment { Orange }
    }
    box {
        <0,BBSize*(2/3),0>,
        <BBSize,0,-.2>
        translate <-BBSize/2,-.5,HoopR+1>
        texture { T_Chrome_4B }
    }
}

#declare Hoop =
union { 
    object { HoopTop
        translate <0,5,0>
    }
    cylinder {
        <0,0,0>
        <0,5.1,0>
        .2
        translate <0,0,3>
        pigment { Black }
        
    }  

}


  
plane { y, 0
    pigment {
      checker pigment { HuntersGreen }   ,
      pigment { P_WoodGrain8A }
    }
    finish {
          ambient 0.1
          diffuse 0.6
          phong .7
          reflection .5
    }
}


plane { z, 50
   pigment {
      color SummerSky
   }
   finish {
      ambient 0.2
      diffuse 0.2
   }
}

 
#declare HX = 6;
#declare HY = 0;
#declare HZ = 15;
#declare HR = 0;

object { Hoop
    
    interior { ior 1.6 }
    finish  {
        ambient 0.1
        diffuse 0.1
        reflection 0.1
        specular 0.8
        roughness 0.003
        phong 1
        phong_size 400
    }
    rotate <0,25,HR> 
    translate <HX,HY,HZ>      
}

#declare BallR = .5;
#declare Bx = 0;
#declare By = 0;
#declare Bz = 0;

#if (clock < 5) 
    #local clk  = 2.5 * clock * (pi/5);
    #declare Bx = -clock;
    #declare By = abs(sin(clk));
    #declare Bz = -clock;    
#end

#if (clock >= 5 & clock < 10) // Ball at <-5,0,-5>
    #local clk = clock-5;    // 0 to 5
    #local clk_pi  = 2.5 * clk * (pi/5);
    #declare Bx = -5 + clk*2;
    #declare By = -((clk-2.5)*(clk-2.5))+7;
    #declare Bz = -5 + clk*1.5;    
#end

sphere {
      <0,0,0>
      BallR
      translate <Bx,BallR+By,Bz>
      pigment { color Orange }
} 

light_source {
    <2, 4, -1>
    color White
}

light_source {
    <2, 4, 1>
    color White
}

light_source {
    <-4,5,-4>
    color White
}

light_source { <0,10,30> color White }