attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec4 a_color;
//attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans;
uniform mat4 u_projTrans;

varying vec4 v_color;

//varying vec2 v_texCoord0;

void main() {
//    v_texCoord0 = a_texCoord0;
    v_color = a_color;

    gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);
}