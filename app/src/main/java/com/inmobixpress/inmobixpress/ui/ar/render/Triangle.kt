package com.inmobixpress.inmobixpress.ui.ar.render

import kotlin.math.abs

class Triangle(
    var V0: FloatArray,
    var V1: FloatArray,
    var V2: FloatArray
) {


    // intersectRayAndTriangle(): intersect a ray with a 3D triangle
//    Input:  a ray R, and a triangle T
//    Output: *I = intersection point (when it exists)
//    Return: -1 = triangle is degenerate (a segment or point)
//             0 = disjoint (no intersect)
//             1 = intersect in unique point I1
//             2 = are in the same plane
    companion object {
        const val SMALL_NUM: Float = 0.00000001f // anything that avoids division overflow
        fun intersectRayAndTriangle(
            near: FloatArray,
            far: FloatArray,
            T: Triangle,
            I: FloatArray
        ): Int {
            val n: FloatArray // triangle vectors
            val r: Float
            val a: Float
            val b: Float // params to calc ray-plane intersect


            // get triangle edge vectors and plane normal
            val u: FloatArray = minus(T.V1, T.V0)
            val v: FloatArray = minus(T.V2, T.V0)
            n = crossProduct(u, v) // cross product

            if (n.contentEquals(
                    floatArrayOf(
                        0.0f,
                        0.0f,
                        0.0f
                    )
                )
            ) {           // triangle is degenerate
                return -1 // do not deal with this case
            }
            val dir: FloatArray = minus(near, far) // ray direction vector
            val w0: FloatArray = minus(far, T.V0)
            a = -dot(n, w0)
            b = dot(n, dir)
            if (abs(b.toDouble()) < SMALL_NUM) {     // ray is parallel to triangle plane
                return if (a == 0f) {                // ray lies in triangle plane
                    2
                } else {
                    0 // ray disjoint from plane
                }
            }

            // get intersect point of ray with triangle plane
            r = a / b
            if (r < 0.0f) {                   // ray goes away from triangle
                return 0 // => no intersect
            }

            // for a segment, also test if (r > 1.0) => no intersect
            val tempI: FloatArray = addition(
                far,
                scalarProduct(r, dir)
            ) // intersect point of ray and plane
            I[0] = tempI[0]
            I[1] = tempI[1]
            I[2] = tempI[2]
            val wu: Float
            val wv: Float
            val D: Float


            // is I inside T?
            val uu: Float = dot(u, u)
            val uv: Float = dot(u, v)
            val vv: Float = dot(v, v)
            val w: FloatArray = minus(I, T.V0) // ray vectors
            wu = dot(w, u)
            wv = dot(w, v)
            D = (uv * uv) - (uu * vv)


            // get and test parametric coords
            val s = ((uv * wv) - (vv * wu)) / D
            if (s < 0.0f || s > 1.0f) // I is outside T
                return 0
            val t = (uv * wu - uu * wv) / D
            if (t < 0.0f || (s + t) > 1.0f) // I is outside T
                return 0

            return 1 // I is in T
        }
    }
}

