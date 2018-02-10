using System;
using System.Collections.Generic;
using System.Text;

namespace Algorithms
{
    class JewelsAndStones
    {
        public int NumJewelsInStones(string J, string S)
        {
            if (J.Length == 0 || S.Length == 0)
                return 0;
            int nums = 0;
            for(int n = 0; n < S.Length; n++)
            {
                for(int m = 0; m < J.Length; m++)
                {
                    if (S[n] == J[m])
                        nums++;
                }
            }
            return nums;
        }
    }
    
}
